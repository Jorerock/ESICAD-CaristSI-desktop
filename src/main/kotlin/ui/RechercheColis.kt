package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ktorm.database.Database
import org.ktorm.dsl.*
import routing.Routes
import ui.Element.FeedbackMessage
import ui.Element.OperationState
import ui.Element.primaryColor
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ktorm.allee
import java.sql.DriverManager
import kotlin.use
import ktorm.colis
import ktorm.colonne_table
import ktorm.etage
import ktorm.place
import ktorm.emplacements
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.Int

// Modèle de données pour le colis
//data class ColisDetails(
//    val id: Int,
//    val longueur: Int,
//    val largeur: Int,
//    val hauteur: Int,
//    val poids: Int
//)

data class ColisDetails(
    val id: Int, // ou le type approprié pour votre ID
    val longueur: Int, // ajustez les types selon vos besoins
    val largeur: Int,
    val hauteur: Int,
    val poids: Int,
    val numeroEtage: Int, // ou String selon le type dans votre base
    val numeroColonne: Int, // ou String selon le type dans votre base
    val numeroAllee: Int, // ou String selon le type dans votre base
    val DateDepose: LocalDate // ou LocalDateTime selon votre implémentation
)

@Composable
fun RechercheColis(onRecherche:(Int)->Unit, onNavigate:(Routes)->Unit) {
    var ID by remember { mutableStateOf("") }
    var feedbackState by remember { mutableStateOf<OperationState<String>>(OperationState.Initial) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // États pour les détails du colis
    var colisFound by remember { mutableStateOf(false) }
    var currentColis by remember { mutableStateOf<ColisDetails?>(null) }

    // États pour l'édition
    var isEditing by remember { mutableStateOf(false) }
    var longueur by remember { mutableStateOf("") }
    var largeur by remember { mutableStateOf("") }
    var hauteur by remember { mutableStateOf("") }
    var poids by remember { mutableStateOf("") }
    val database = org.koin.core.context.GlobalContext.get().get<Database>()

    // Fonction pour rechercher un colis
    fun rechercheMonColis(idColis: Int) {
        try {
            // Recherche dans la base de données
            // Définissez clairement les tables avec des noms uniques
            val colisTable = colis
            val placeTable = place
            val emplacementTable = emplacements
            val etageTable = etage
            val colonneTable = colonne_table  // Utiliser cette variable partout de manière cohérente
            val alleeTable = allee

            val result = database?.from(colisTable)
                ?.innerJoin(placeTable, on = colisTable.ID eq placeTable.ID_Colis)
                ?.innerJoin(emplacementTable, on = placeTable.ID_Emplacement eq emplacementTable.ID_Emplacement)
                ?.innerJoin(etageTable, on = emplacementTable.ID_Etage eq etageTable.ID_Etage)
                ?.innerJoin(colonneTable, on = etageTable.ID_Colonne eq colonne_table.ID_Colonne) // Utiliser colonneTable, pas colonne
                ?.innerJoin(alleeTable, on = colonneTable.ID_Allee eq alleeTable.ID_Allee)
                ?.select()
                ?.where { colisTable.ID eq idColis }
                ?.map { row ->
                    ColisDetails(
                        id = row[colisTable.ID]!!,
                        longueur = row[colisTable.Longueur]!!,
                        largeur = row[colisTable.Largeur]!!,
                        hauteur = row[colisTable.Hauteur]!!,
                        poids = row[colisTable.Poids]!!,
                        numeroEtage = row[etageTable.Numero_Etage]!!,
                        numeroColonne = row[colonneTable.NumeroCol]!!,
                        numeroAllee = row[alleeTable.Numero]!!,
                        DateDepose = LocalDate.now()
                    )
                }
                ?.firstOrNull()

            // Si aucun résultat de la DB ou pour tester sans DB, créer un colis fictif
            if (result != null) {
                // Colis trouvé dans la base de données
                currentColis = result
                longueur = result.longueur.toString()
                largeur = result.largeur.toString()
                hauteur = result.hauteur.toString()
                poids = result.poids.toString()
                colisFound = true
                feedbackState = OperationState.Success("Colis trouvé dans la base de données!")
            } else if (idColis == 123) { // Colis fictif pour tests avec ID = 123
                // Créer un colis fictif pour les tests
                val testColis = ColisDetails(
                    id = 123,
                    longueur = 50,
                    largeur = 30,
                    hauteur = 20,
                    poids = 5,
                    numeroEtage = 1,
                    numeroColonne = 1,
                    numeroAllee = 1,
                    DateDepose = LocalDate.now()
                )
                currentColis = testColis
                longueur = testColis.longueur.toString()
                largeur = testColis.largeur.toString()
                hauteur = testColis.hauteur.toString()
                poids = testColis.poids.toString()
                colisFound = true
                feedbackState = OperationState.Success("Colis de test trouvé!")
            } else {
                // Colis non trouvé
                colisFound = false
                feedbackState = OperationState.Error("Colis #$idColis non trouvé")
            }

            GlobalScope.launch {
                delay(2000)
                feedbackState = OperationState.Initial
            }

        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la recherche: ${e.message}")
            colisFound = false
        }
    }
// Variables pour stocker les données de référence
    var alleesDisponibles by remember { mutableStateOf<List<Int>>(emptyList()) }
    var colonnesDisponibles by remember { mutableStateOf<List<Int>>(emptyList()) }
    var etagesDisponibles by remember { mutableStateOf<List<Int>>(emptyList()) }

    // Variables pour les dropdowns
    var expandedAllee by remember { mutableStateOf(false) }
    var expandedColonne by remember { mutableStateOf(false) }
    var expandedEtage by remember { mutableStateOf(false) }

    // Variables pour stocker les sélections temporaires
    var selectedAllee by remember { mutableStateOf<Int?>(null) }
    var selectedColonne by remember { mutableStateOf<Int?>(null) }
    var selectedEtage by remember { mutableStateOf<Int?>(null) }

    // Fonction pour récupérer les données de référence
    fun loadReferenceData() {
        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                // Récupérer les allées
                val alleeQuery = "SELECT DISTINCT Numero FROM allee ORDER BY Numero"
                connection.prepareStatement(alleeQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val allees = mutableListOf<Int>()
                    while (resultSet.next()) {
                        allees.add(resultSet.getInt("Numero"))
                    }
                    alleesDisponibles = allees
                }

                // Récupérer les colonnes
                val colonneQuery = "SELECT DISTINCT NumeroCol FROM colonne ORDER BY NumeroCol"
                connection.prepareStatement(colonneQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val colonnes = mutableListOf<Int>()
                    while (resultSet.next()) {
                        colonnes.add(resultSet.getInt("NumeroCol"))
                    }
                    colonnesDisponibles = colonnes
                }

                // Récupérer les étages
                val etageQuery = "SELECT DISTINCT Numero_Etage FROM etage ORDER BY Numero_Etage"
                connection.prepareStatement(etageQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val etages = mutableListOf<Int>()
                    while (resultSet.next()) {
                        etages.add(resultSet.getInt("Numero_Etage"))
                    }
                    etagesDisponibles = etages
                }
            }
        } catch (e: Exception) {
            println("Erreur lors du chargement des données de référence: ${e.message}")
        }
    }

    // Charger les données de référence au démarrage
    LaunchedEffect(Unit) {
        loadReferenceData()
    }

    fun rechercheMonColisAlternative(idColis: Int) {
        try {
            // Skip Ktorm and go directly with JDBC for troubleshooting
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                val query = """
                SELECT 
                    c.ID_Colis, 
                    c.Longueur, 
                    c.Largeur, 
                    c.Hauteur, 
                    c.Poids,
                    a.Numero AS Numero_Allee, 
                    col.NumeroCol, 
                    e.Numero_Etage
                FROM colis c
                JOIN place p ON c.ID_Colis = p.ID_Colis
                JOIN emplacement em ON p.ID_Emplacement = em.ID_Emplacement
                JOIN etage e ON em.ID_Etage = e.ID_Etage
                JOIN colonne col ON e.ID_Colonne = col.ID_Colonne
                JOIN allee a ON col.ID_Allee = a.ID_Allee
                WHERE c.ID_Colis = ?
            """

                connection.prepareStatement(query).use { statement ->
                    statement.setInt(1, idColis)
                    val resultSet = statement.executeQuery()

                    if (resultSet.next()) {
                        val result = ColisDetails(
                            id = resultSet.getInt("ID_Colis"),
                            longueur = resultSet.getInt("Longueur"),
                            largeur = resultSet.getInt("Largeur"),
                            hauteur = resultSet.getInt("Hauteur"),
                            poids = resultSet.getInt("Poids"),
                            numeroAllee = resultSet.getInt("Numero_Allee"),
                            numeroColonne = resultSet.getInt("NumeroCol"),
                            numeroEtage = resultSet.getInt("Numero_Etage"),
                            DateDepose = LocalDate.now()
                        )

                        currentColis = result
                        longueur = result.longueur.toString()
                        largeur = result.largeur.toString()
                        hauteur = result.hauteur.toString()
                        poids = result.poids.toString()
                        // Initialiser les sélections avec les valeurs actuelles
                        selectedAllee = result.numeroAllee
                        selectedColonne = result.numeroColonne
                        selectedEtage = result.numeroEtage
                        colisFound = true
                        feedbackState = OperationState.Success("Colis trouvé dans la base de données!")
                    } else if (idColis == 123) {
                        // Test case remains the same
                        val testColis = ColisDetails(
                            id = 123,
                            longueur = 50,
                            largeur = 30,
                            hauteur = 20,
                            poids = 5,
                            numeroEtage = 1,
                            numeroColonne = 1,
                            numeroAllee = 1,
                            DateDepose = LocalDate.now()
                        )
                        currentColis = testColis
                        longueur = testColis.longueur.toString()
                        largeur = testColis.largeur.toString()
                        hauteur = testColis.hauteur.toString()
                        poids = testColis.poids.toString()
                        selectedAllee = testColis.numeroAllee
                        selectedColonne = testColis.numeroColonne
                        selectedEtage = testColis.numeroEtage
                        colisFound = true
                        feedbackState = OperationState.Success("Colis de test trouvé!")
                    } else {
                        colisFound = false
                        feedbackState = OperationState.Error("Colis #$idColis non trouvé")
                    }
                }
            }

            GlobalScope.launch {
                delay(2000)
                feedbackState = OperationState.Initial
            }
        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la recherche: ${e.message}")
            colisFound = false
            println("Exception détaillée: ${e.stackTraceToString()}")
        }
    }

    // Fonction pour mettre à jour un colis avec nouvel emplacement
    fun updateColisWithLocation() {
        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            val updatedColis = ColisDetails(
                id = currentColis!!.id,
                longueur = longueur.toInt(),
                largeur = largeur.toInt(),
                hauteur = hauteur.toInt(),
                poids = poids.toInt(),
                numeroEtage = selectedEtage ?: currentColis!!.numeroEtage,
                numeroColonne = selectedColonne ?: currentColis!!.numeroColonne,
                numeroAllee = selectedAllee ?: currentColis!!.numeroAllee,
                DateDepose = LocalDate.now()
            )

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                connection.autoCommit = false

                try {
                    // Mettre à jour les informations du colis
                    val updateColisQuery = """
                        UPDATE colis 
                        SET Longueur = ?, Largeur = ?, Hauteur = ?, Poids = ? 
                        WHERE ID_Colis = ?
                    """
                    connection.prepareStatement(updateColisQuery).use { statement ->
                        statement.setInt(1, updatedColis.longueur)
                        statement.setInt(2, updatedColis.largeur)
                        statement.setInt(3, updatedColis.hauteur)
                        statement.setInt(4, updatedColis.poids)
                        statement.setInt(5, updatedColis.id)
                        statement.executeUpdate()
                    }

                    // Si l'emplacement a changé, mettre à jour la place
                    if (selectedAllee != currentColis!!.numeroAllee ||
                        selectedColonne != currentColis!!.numeroColonne ||
                        selectedEtage != currentColis!!.numeroEtage) {

                        // Trouver le nouvel emplacement
                        val findEmplacementQuery = """
                            SELECT em.ID_Emplacement
                            FROM emplacement em
                            JOIN etage e ON em.ID_Etage = e.ID_Etage
                            JOIN colonne col ON e.ID_Colonne = col.ID_Colonne
                            JOIN allee a ON col.ID_Allee = a.ID_Allee
                            WHERE a.Numero = ? AND col.NumeroCol = ? AND e.Numero_Etage = ?
                            LIMIT 1
                        """

                        var nouvelEmplacementId: Int? = null
                        connection.prepareStatement(findEmplacementQuery).use { statement ->
                            statement.setInt(1, updatedColis.numeroAllee)
                            statement.setInt(2, updatedColis.numeroColonne)
                            statement.setInt(3, updatedColis.numeroEtage)
                            val resultSet = statement.executeQuery()
                            if (resultSet.next()) {
                                nouvelEmplacementId = resultSet.getInt("ID_Emplacement")
                            }
                        }

                        nouvelEmplacementId?.let { newId ->
                            // Mettre à jour la place
                            val updatePlaceQuery = """
                                UPDATE place 
                                SET ID_Emplacement = ? 
                                WHERE ID_Colis = ?
                            """
                            connection.prepareStatement(updatePlaceQuery).use { statement ->
                                statement.setInt(1, newId)
                                statement.setInt(2, updatedColis.id)
                                statement.executeUpdate()
                            }
                        }
                    }

                    connection.commit()
                    currentColis = updatedColis
                    isEditing = false
                    feedbackState = OperationState.Success("Colis mis à jour avec succès!")

                } catch (e: Exception) {
                    connection.rollback()
                    throw e
                }
            }

            GlobalScope.launch {
                delay(2000)
                feedbackState = OperationState.Initial
            }

        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la mise à jour: ${e.message}")
            println("Erreur détaillée: ${e.stackTraceToString()}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // En-tête avec titre et boutons d'action
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestion des Colis",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // Boutons d'action
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigate(Routes.HOME) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text("Retour à l'accueil", color = Color.White)
                }

                Button(
                    onClick = { onNavigate(Routes.CREATECOLIS) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                ) {
                    Text("Nouveau Colis", color = Color.White)
                }
            }
        }

        // Feedback d'opération
        AnimatedVisibility(
            visible = feedbackState !is OperationState.Initial,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            when (val state = feedbackState) {
                is OperationState.Success -> {
                    FeedbackMessage(message = state.data, isError = false)
                }
                is OperationState.Error -> {
                    FeedbackMessage(message = state.message, isError = true)
                }
                else -> {}
            }
        }

        // Carte de recherche
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Rechercher un colis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = ID,
                        onValueChange = { newValue ->
                            // Validation: accepter uniquement les chiffres
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                ID = newValue
                                errorMessage = null
                            }
                        },
                        label = { Text("Identifiant du colis") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = errorMessage != null
                    )

                    Button(
                        onClick = {
                            if (ID.isEmpty()) {
                                errorMessage = "Veuillez entrer un identifiant"
                            } else {
                                try {
                                    val idColis = ID.toInt()
                                    rechercheMonColisAlternative(idColis)
                                } catch (e: Exception) {
                                    errorMessage = "Identifiant invalide"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
                    ) {
                        Text("Rechercher", color = Color.White)
                    }
                }

                errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }
            }
        }

        // Affichage des détails du colis trouvé
        AnimatedVisibility(
            visible = colisFound,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Détails du colis #${currentColis?.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Button(
                            onClick = {
                                isEditing = !isEditing
                                if (!isEditing) {
                                    // Réinitialiser les valeurs si on annule l'édition
                                    currentColis?.let {
                                        longueur = it.longueur.toString()
                                        largeur = it.largeur.toString()
                                        hauteur = it.hauteur.toString()
                                        poids = it.poids.toString()
                                        selectedAllee = it.numeroAllee
                                        selectedColonne = it.numeroColonne
                                        selectedEtage = it.numeroEtage
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (isEditing) Color.Gray else Color.Blue
                            )
                        ) {
                            Text(
                                if (isEditing) "Annuler" else "Modifier",
                                color = Color.White
                            )
                        }
                    }

                    // Grille de détails / formulaire d'édition
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Longueur
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Longueur:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                OutlinedTextField(
                                    value = longueur,
                                    onValueChange = { newValue ->
                                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                            longueur = newValue
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            } else {
                                Text("${currentColis?.longueur} cm")
                            }
                        }

                        // Largeur
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Largeur:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                OutlinedTextField(
                                    value = largeur,
                                    onValueChange = { newValue ->
                                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                            largeur = newValue
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            } else {
                                Text("${currentColis?.largeur} cm")
                            }
                        }

                        // Hauteur
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Hauteur:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                OutlinedTextField(
                                    value = hauteur,
                                    onValueChange = { newValue ->
                                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                            hauteur = newValue
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            } else {
                                Text("${currentColis?.hauteur} cm")
                            }
                        }

                        // Poids
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Poids:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                OutlinedTextField(
                                    value = poids,
                                    onValueChange = { newValue ->
                                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                            poids = newValue
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            } else {
                                Text("${currentColis?.poids} kg")
                            }
                        }

                        // Section d'affichage/modification de l'emplacement
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            "Emplacement du colis",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Numéro d'Allée
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Allée:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = "Allée ${selectedAllee ?: currentColis?.numeroAllee}",
                                        onValueChange = { },
                                        enabled = false,
                                        modifier = Modifier.fillMaxWidth().clickable { expandedAllee = true },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (expandedAllee) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    DropdownMenu(
                                        expanded = expandedAllee,
                                        onDismissRequest = { expandedAllee = false }
                                    ) {
                                        alleesDisponibles.forEach { allee ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedAllee = allee
                                                    expandedAllee = false
                                                }
                                            ) {
                                                Text("Allée $allee")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("${currentColis?.numeroAllee}")
                            }
                        }

                        // Numéro de Colonne
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Colonne:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = "Colonne ${selectedColonne ?: currentColis?.numeroColonne}",
                                        onValueChange = { },
                                        enabled = false,
                                        modifier = Modifier.fillMaxWidth().clickable { expandedColonne = true },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (expandedColonne) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    DropdownMenu(
                                        expanded = expandedColonne,
                                        onDismissRequest = { expandedColonne = false }
                                    ) {
                                        colonnesDisponibles.forEach { colonne ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedColonne = colonne
                                                    expandedColonne = false
                                                }
                                            ) {
                                                Text("Colonne $colonne")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("${currentColis?.numeroColonne}")
                            }
                        }

                        // Numéro d'Étage
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Étage:",
                                modifier = Modifier.width(120.dp),
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditing) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = "Étage ${selectedEtage ?: currentColis?.numeroEtage}",
                                        onValueChange = { },
                                        enabled = false,
                                        modifier = Modifier.fillMaxWidth().clickable { expandedEtage = true },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (expandedEtage) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                    )

                                    DropdownMenu(
                                        expanded = expandedEtage,
                                        onDismissRequest = { expandedEtage = false }
                                    ) {
                                        etagesDisponibles.forEach { etage ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedEtage = etage
                                                    expandedEtage = false
                                                }
                                            ) {
                                                Text("Étage $etage")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("${currentColis?.numeroEtage}")
                            }
                        }

                        // Bouton de sauvegarde en mode édition
                        if (isEditing) {
                            Button(
                                onClick = { updateColisWithLocation() },
                                colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                            ) {
                                Text("Enregistrer les modifications", color = Color.White)
                            }
                        }
                    }

                    // Affichage du volume et autres informations calculées
                    if (!isEditing && currentColis != null) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Volume:",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "${currentColis!!.longueur * currentColis!!.largeur * currentColis!!.hauteur} cm³",
                                    color = Color.DarkGray
                                )
                            }

                            Column {
                                Text(
                                    "Dimensions totales:",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "${currentColis!!.longueur} × ${currentColis!!.largeur} × ${currentColis!!.hauteur} cm",
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Message d'information lorsqu'aucun colis n'est trouvé
        if (!colisFound) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        "Entrez l'identifiant d'un colis pour afficher ses détails et le modifier si nécessaire. " +
                                "Vous pouvez également créer un nouveau colis en cliquant sur le bouton \"Nouveau Colis\".",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}