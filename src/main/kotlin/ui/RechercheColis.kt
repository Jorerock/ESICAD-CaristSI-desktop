package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
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
import java.sql.DriverManager
import kotlin.use

// Modèle de données pour le colis
data class ColisDetails(
    val id: Int,
    val longueur: Int,
    val largeur: Int,
    val hauteur: Int,
    val poids: Int
)

@Composable
fun RechercheColis(onRecherche:(Int)->Unit, onNavigate:(Routes)->Unit, database: Database? = null) {
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

    // Fonction pour rechercher un colis
    fun rechercheColis(idColis: Int) {
        try {
            // Simulation de recherche - à remplacer par votre logique réelle
            // Normalement, vous feriez un appel à votre base de données ici
            // Exemple fictif:
            /*
            val result = database?.from(colis)
                ?.select()
                ?.where { colis.id eq idColis }
                ?.map { row ->
                    ColisDetails(
                        id = row[colis.id]!!,
                        longueur = row[colis.longueur]!!,
                        largeur = row[colis.largeur]!!,
                        hauteur = row[colis.hauteur]!!,
                        poids = row[colis.poids]!!
                    )
                }
                ?.firstOrNull()
            */

            // Pour la démo, créons un colis fictif:
            val mockColis = ColisDetails(
                id = idColis,
                longueur = 30,
                largeur = 20,
                hauteur = 15,
                poids = 2
            )

            currentColis = mockColis
            longueur = mockColis.longueur.toString()
            largeur = mockColis.largeur.toString()
            hauteur = mockColis.hauteur.toString()
            poids = mockColis.poids.toString()
            colisFound = true

            feedbackState = OperationState.Success("Colis trouvé!")

            GlobalScope.launch {
                delay(2000)
                feedbackState = OperationState.Initial
            }

            onRecherche(idColis) // Appel de la fonction de callback

        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la recherche: ${e.message}")
            colisFound = false
        }
    }

    // Fonction pour mettre à jour un colis
    fun updateColis() {
        try {
            val updatedColis = ColisDetails(
                id = currentColis!!.id,
                longueur = longueur.toInt(),
                largeur = largeur.toInt(),
                hauteur = hauteur.toInt(),
                poids = poids.toInt()
            )

            // Ici, vous implémenteriez la logique pour mettre à jour la base de données
            // Exemple:
            /*
            database?.update(colis) {
                set(colis.longueur, updatedColis.longueur)
                set(colis.largeur, updatedColis.largeur)
                set(colis.hauteur, updatedColis.hauteur)
                set(colis.poids, updatedColis.poids)
                where {
                    colis.id eq updatedColis.id
                }
            }
            */

            currentColis = updatedColis
            isEditing = false
            feedbackState = OperationState.Success("Colis mis à jour avec succès!")

            GlobalScope.launch {
                delay(2000)
                feedbackState = OperationState.Initial
            }

        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la mise à jour: ${e.message}")
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
                    Text("Retour à l'accueil")
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
                                    rechercheColis(idColis)
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

                        // Bouton de sauvegarde en mode édition
                        if (isEditing) {
                            Button(
                                onClick = { updateColis() },
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

//@Composable
//fun InfoColis(database: Database, ColisSelectione: Int, onNavigate: (Routes) -> Unit) {
//    var Stocks = requestColis(ColisSelectione)
//
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
//    ) {
//        Column {
//            Button(
//                onClick = {
//                    onNavigate(Routes.RECHERCHE)  // Retour à la recherche sans changer de route
//                },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text("Retour")
//            }
//
//            // Le reste de votre code InfoColis reste identique
//            // Table Rows
//            Stocks.forEach { Stock ->
//                var ID_Colis by remember { mutableStateOf(Stock.ID_Colis.toString()) }
//                var Longueur by remember { mutableStateOf((Stock.Longueur.toString())) }
//                var Largeur by remember { mutableStateOf(Stock.Largeur.toString()) }
//                var Hauteur by remember { mutableStateOf(Stock.Hauteur.toString()) }
//                var Poids by remember { mutableStateOf(Stock.Poids.toString()) }
//
//                var ID_Allee by remember { mutableStateOf(Stock.ID_Allee.toString()) }
//                var Numero by remember { mutableStateOf(Stock.Numero_Allee.toString()) }
//                var ID_Colonne by remember { mutableStateOf(Stock.ID_Colonne.toString()) }
//                var NumeroCol by remember { mutableStateOf(Stock.NumeroCol.toString()) }
//                var ID_Etage by remember { mutableStateOf(Stock.ID_Etage.toString()) }
//                var Numero_Etage by remember { mutableStateOf(Stock.Numero_Etage.toString()) }
//                var ID_Emplacement by remember { mutableStateOf(Stock.ID_Emplacement.toString()) }
//                var VolumeMax by remember { mutableStateOf(Stock.VolumeMax.toString()) }
//                var PoidsMax by remember { mutableStateOf(Stock.PoidsMax.toString()) }
//                var result by remember { mutableStateOf("") }
//
//                Row {
//                    Text(Stock.ID_Colis.toString(), modifier = Modifier.weight(1f))
//                    OutlinedTextField(
//                        value = Longueur,
//                        onValueChange = { newValue ->
//                            Longueur = newValue
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Longueur = numCol.toString()
//                            }
//                        },
//                        label = { Text("Longueur") },
//                        modifier = Modifier.weight(2f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    OutlinedTextField(
//                        value = Largeur,
//                        onValueChange = { newValue ->
//                            Largeur = newValue
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Largeur = numCol.toString()
//                            }
//                        },
//                        label = { Text("Largeur") },
//                        modifier = Modifier.weight(3f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    OutlinedTextField(
//                        value = Hauteur,
//                        onValueChange = { newValue ->
//                            Hauteur = newValue
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Hauteur = numCol.toString()
//                            }
//                        },
//                        label = { Text("Hauteur") },
//                        modifier = Modifier.weight(4f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    OutlinedTextField(
//                        value = Poids,
//                        onValueChange = { newValue ->
//                            Poids = newValue
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Poids = numCol.toString()
//                            }
//                        },
//                        label = { Text("Poids") },
//                        modifier = Modifier.weight(5f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                }
//
//                // Reste du code identique...
//
//                // ... Garder le reste de vos composants et fonctions inchangés
//            }
//        }
//    }
//}


fun requestColis(ColisSelectione: Int ): List<Stock> {
    val Stocks = mutableListOf<Stock>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        println(ColisSelectione)
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
//            val query = "SELECT * FROM `colis` WHERE ID_Colis = '"+ColisSelectione.toString()+"'"
            val query = "select * from colis\n" +
                    "join place on colis.ID_Colis = place.ID_Colis\n" +
                    "join emplacement on place.ID_Emplacement = emplacement.ID_Emplacement\n" +
                    "join etage on etage.ID_Etage = emplacement.ID_Etage\n" +
                    "join colonne on etage.ID_Colonne = colonne.ID_Colonne\n" +
                    "join allee on colonne.ID_Allee = allee.ID_Allee\n" +
                    " WHERE colis.ID_Colis ='"+ColisSelectione.toString()+"'"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    Stocks.add(
                        Stock(
                            ID_Colis = resultSet.getInt("colis.ID_Colis"),
                            Longueur = resultSet.getInt("colis.Longueur"),
                            Largeur = resultSet.getInt("colis.Largeur") ,
                            Hauteur = resultSet.getInt("colis.Hauteur"),
                            Poids = resultSet.getInt("colis.Poids"),
                            Numero_Allee = resultSet.getInt("allee.Numero"),
                            ID_Allee  = resultSet.getInt("allee.ID_Allee"),
                            ID_Colonne  = resultSet.getInt("colonne.ID_Colonne"),
                            NumeroCol  = resultSet.getInt("colonne.NumeroCol"),
                            ID_Etage  = resultSet.getInt("etage.ID_Etage"),
                            Numero_Etage  = resultSet.getInt("etage.Numero_Etage"),
                            ID_Emplacement  = resultSet.getInt("emplacement.ID_Emplacement"),
                            VolumeMax  = resultSet.getInt("emplacement.VolumeMax"),
                            PoidsMax  = resultSet.getInt("emplacement.PoidsMax"),
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(Stocks)
    return Stocks
}

data class Stock(
    val ID_Colis: Int,
    val Longueur: Int,
    val Largeur: Int,
    val Hauteur: Int,
    val Poids: Int,
    val ID_Allee: Int,
    val Numero_Allee: Int,
    val ID_Colonne: Int,
    val NumeroCol: Int,
    val ID_Etage: Int,
    val Numero_Etage: Int,
    val ID_Emplacement: Int,
    val VolumeMax: Int,
    val PoidsMax: Int
)
