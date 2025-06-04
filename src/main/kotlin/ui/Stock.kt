//package ui
//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import ui.LoginScreen
//import routing.Router
//import routing.Routes
//import ui.HomeScreen
//import ui.LoginScreen
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import ktorm.Caristes
//import ktorm.colis
//import org.ktorm.database.Database
//import org.ktorm.dsl.*
//
//import java.sql.DriverManager
//
//
//
//@Composable
//fun StockScreen(database : Database,AlleeSelectione : Int ,onRechercheColis:(Int)->Unit, onNavigate:(Routes)->Unit) {
//    var Selection = AlleeSelectione
//    var Stocks = request(Selection)
//
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
//    ){
//
//        Column {
//            Button(
//                onClick = {
//                    onNavigate(Routes.HOME)
//                },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text("Retour")
//            }
//            Text("Voicie les colis de l'emplacement selectionne : ")
//
//
//            // Table Header
//            Row {
//                Text("Id_Colis", modifier = Modifier.padding(vertical = 8.dp).weight(1f))
//                Text("Longueur", modifier = Modifier.padding(vertical = 8.dp).weight(2f))
//                Text("Largeur", modifier = Modifier.padding(vertical = 8.dp).weight(3f))
//                Text("Hauteur", modifier = Modifier.padding(vertical = 8.dp).weight(4f))
//                Text("Poids", modifier = Modifier.padding(vertical = 8.dp).weight(5f))
//
//            }
//            // Table Rows
//            Stocks.forEach { Stock ->
//                var ID_Colis by remember { mutableStateOf(Stock.ID_Colis) }
//                var Longueur by remember { mutableStateOf((Stock.Longueur.toString())) }
//                var Largeur by remember { mutableStateOf(Stock.Largeur.toString())}
//                var Hauteur by remember { mutableStateOf(Stock.Hauteur.toString())}
//                var Poids by remember { mutableStateOf(Stock.Poids.toString())}
//
//                Row {
//                    Button(
//                        onClick = {
//                            onRechercheColis(ID_Colis)
////                            onNavigate(Routes.EMPLACEMENT)
//                        },
//                        modifier = Modifier.padding(vertical = 8.dp).weight(1f)
//                    ) {
//                        Text(ID_Colis.toString())
//                    }
//
//                    Text(Longueur, modifier = Modifier.padding(vertical = 8.dp).weight(2f))
//                    Text(Largeur, modifier = Modifier.padding(vertical = 8.dp).weight(3f))
//                    Text(Hauteur, modifier = Modifier.padding(vertical = 8.dp).weight(4f))
//                    Text(Poids, modifier = Modifier.padding(vertical = 8.dp).weight(5f))
//
//
////                    OutlinedTextField(
////                        value = Longueur,
////                        onValueChange = { newValue ->
////                            // Vous pouvez ajouter une validation ici si nécessaire
////                            Longueur = newValue
////                            // Pour mettre à jour la valeur dans votre modèle:
////                            // Convertir en Int avec gestion d'erreur
////                            newValue.toIntOrNull()?.let { numCol ->
////                                Longueur = numCol.toString()
////                            }
////                        },
////                        label = { Text("Longueur") },
////                        modifier = Modifier.weight(2f),
////                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
////                    )
////
////                    OutlinedTextField(
////                        value = Largeur,
////                        onValueChange = { newValue ->
////                            // Vous pouvez ajouter une validation ici si nécessaire
////                            Largeur = newValue
////                            // Pour mettre à jour la valeur dans votre modèle:
////                            // Convertir en Int avec gestion d'erreur
////                            newValue.toIntOrNull()?.let { numCol ->
////                                Largeur = numCol.toString()
////                            }
////                        },
////                        label = { Text("Largeur") },
////                        modifier = Modifier.weight(3f),
////                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
////                    )
//
////                    Button(
////                        onClick = {
////                            val updatedStocks = Stock(
////                                ID_Colis = NumeroCol.toInt(),
////                                Longueur = Longueur.toInt(),
////                                Largeur = Largeur.toInt()
////                            )
////                            sendData(database, updatedStocks)
////                                  },
////
////                        modifier = Modifier.weight(4f).padding(vertical = 8.dp)
////                    ) {
////                        Text("Modifier")
////                    }
//                }
//            }
//        }
//    }
//}
//
//
//fun request(AlleeSelectione: Int ): List<Stock> {
//    val Stocks = mutableListOf<Stock>()
//    val url = "jdbc:mysql://localhost:3306/carist-si"
//    val user = "root"
//    val password = ""
//
//
//    try {
//        Class.forName("com.mysql.cj.jdbc.Driver")
//        DriverManager.getConnection(url, user, password).use { connection ->
//            val query = "select * from colis \n" +
//                    "join place on colis.ID_Colis = place.ID_Colis \n" +
//                    "join emplacement on place.ID_Emplacement = emplacement.ID_Emplacement \n" +
//                    "join etage on etage.ID_Etage = emplacement.ID_Etage\n" +
//                    "join colonne on etage.ID_Colonne = colonne.ID_Colonne \n" +
//                    "join allee on colonne.ID_Allee = allee.ID_Allee\n" +
//                    "where emplacement.ID_Emplacement = '"+AlleeSelectione.toString()+"'"
//            connection.createStatement().use { statement ->
//                val resultSet = statement.executeQuery(query)
//                while (resultSet.next()) {
//                    Stocks.add(
//                        Stock(
//                            ID_Colis = resultSet.getInt("colis.ID_Colis"),
//                            Longueur = resultSet.getInt("colis.Longueur"),
//                            Largeur = resultSet.getInt("colis.Largeur"),
//                            Hauteur = resultSet.getInt("colis.Hauteur"),
//                            Poids = resultSet.getInt("colis.Poids")
//                        )
//                    )
//                }
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    println("Stocks trouve : "+Stocks)
//    return Stocks
//}
//
//data class Stock(
//    val ID_Colis: Int,
//    val Longueur: Int,
//    val Largeur: Int,
//    val Hauteur: Int,
//    val Poids: Int,
//
//)
//
////fun sendData(database : Database, stock : Stock): String {
////
////    try {
////
////        println("update cariste"+ stock)
////        database.update(Caristes) {
////            set(colis.Largeur, stock.Largeur)
////            set(colis.Longueur, stock.Longueur)
////            where {
////                Caristes.ID_cariste eq stock.ID_Colis
////            }
////        }
////
////    } catch (e: Exception) {
////        e.printStackTrace()
////    }
////    return "done"
////}
//


package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import routing.Routes
import ui.Element.FeedbackMessage
import ui.Element.OperationState
import ui.Element.primaryColor
import java.sql.DriverManager
import kotlin.use

// Modèles de données
data class AlleeDetails(
    val id: Int,
    val numero: Int
)

data class ColonneDetails(
    val id: Int,
    val numeroCol: Int,
    val idAllee: Int,
    val numeroAllee: Int? = null
)

data class EtageDetails(
    val id: Int,
    val numeroEtage: Int,
    val idColonne: Int,
    val numeroColonne: Int? = null,
    val numeroAllee: Int? = null
)

enum class StructureType {
    ALLEE, COLONNE, ETAGE
}

@Composable
fun GestionStructures(onNavigate: (Routes) -> Unit) {
    var selectedTab by remember { mutableStateOf(StructureType.ALLEE) }
    var feedbackState by remember { mutableStateOf<OperationState<String>>(OperationState.Initial) }

    // États pour les listes
    var allees by remember { mutableStateOf<List<AlleeDetails>>(emptyList()) }
    var colonnes by remember { mutableStateOf<List<ColonneDetails>>(emptyList()) }
    var etages by remember { mutableStateOf<List<EtageDetails>>(emptyList()) }

    // États pour l'édition
    var isCreating by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<Any?>(null) }

    // Champs de formulaire
    var numeroField by remember { mutableStateOf("") }
    var selectedParentId by remember { mutableStateOf<Int?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }

    // Options pour les dropdowns
    var availableAllees by remember { mutableStateOf<List<AlleeDetails>>(emptyList()) }
    var availableColonnes by remember { mutableStateOf<List<ColonneDetails>>(emptyList()) }

    // Fonction pour charger les données
    fun loadData() {
        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->

                // Charger les allées
                val alleeQuery = "SELECT ID_Allee, Numero FROM allee ORDER BY Numero"
                connection.prepareStatement(alleeQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val alleeList = mutableListOf<AlleeDetails>()
                    while (resultSet.next()) {
                        alleeList.add(
                            AlleeDetails(
                                id = resultSet.getInt("ID_Allee"),
                                numero = resultSet.getInt("Numero")
                            )
                        )
                    }
                    allees = alleeList
                    availableAllees = alleeList
                }

                // Charger les colonnes avec information de l'allée
                val colonneQuery = """
                    SELECT c.ID_Colonne, c.NumeroCol, c.ID_Allee, a.Numero as Numero_Allee
                    FROM colonne c
                    LEFT JOIN allee a ON c.ID_Allee = a.ID_Allee
                    ORDER BY a.Numero, c.NumeroCol
                """
                connection.prepareStatement(colonneQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val colonneList = mutableListOf<ColonneDetails>()
                    while (resultSet.next()) {
                        colonneList.add(
                            ColonneDetails(
                                id = resultSet.getInt("ID_Colonne"),
                                numeroCol = resultSet.getInt("NumeroCol"),
                                idAllee = resultSet.getInt("ID_Allee"),
                                numeroAllee = resultSet.getInt("Numero_Allee")
                            )
                        )
                    }
                    colonnes = colonneList
                    availableColonnes = colonneList
                }

                // Charger les étages avec informations de colonne et allée
                val etageQuery = """
                    SELECT e.ID_Etage, e.Numero_Etage, e.ID_Colonne, 
                           c.NumeroCol, a.Numero as Numero_Allee
                    FROM etage e
                    LEFT JOIN colonne c ON e.ID_Colonne = c.ID_Colonne
                    LEFT JOIN allee a ON c.ID_Allee = a.ID_Allee
                    ORDER BY a.Numero, c.NumeroCol, e.Numero_Etage
                """
                connection.prepareStatement(etageQuery).use { statement ->
                    val resultSet = statement.executeQuery()
                    val etageList = mutableListOf<EtageDetails>()
                    while (resultSet.next()) {
                        etageList.add(
                            EtageDetails(
                                id = resultSet.getInt("ID_Etage"),
                                numeroEtage = resultSet.getInt("Numero_Etage"),
                                idColonne = resultSet.getInt("ID_Colonne"),
                                numeroColonne = resultSet.getInt("NumeroCol"),
                                numeroAllee = resultSet.getInt("Numero_Allee")
                            )
                        )
                    }
                    etages = etageList
                }
            }
        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors du chargement: ${e.message}")
        }
    }

    // Fonction pour créer un nouvel élément
    fun createElement() {
        if (numeroField.isEmpty()) {
            feedbackState = OperationState.Error("Veuillez entrer un numéro")
            return
        }

        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                when (selectedTab) {
                    StructureType.ALLEE -> {
                        val query = "INSERT INTO allee (Numero) VALUES (?)"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Allée créée avec succès!")
                    }

                    StructureType.COLONNE -> {
                        if (selectedParentId == null) {
                            feedbackState = OperationState.Error("Veuillez sélectionner une allée")
                            return
                        }
                        val query = "INSERT INTO colonne (NumeroCol, ID_Allee) VALUES (?, ?)"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.setInt(2, selectedParentId!!)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Colonne créée avec succès!")
                    }

                    StructureType.ETAGE -> {
                        if (selectedParentId == null) {
                            feedbackState = OperationState.Error("Veuillez sélectionner une colonne")
                            return
                        }
                        val query = "INSERT INTO etage (Numero_Etage, ID_Colonne) VALUES (?, ?)"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.setInt(2, selectedParentId!!)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Étage créé avec succès!")
                    }
                }

                // Réinitialiser le formulaire
                numeroField = ""
                selectedParentId = null
                isCreating = false
                loadData()
            }
        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la création: ${e.message}")
        }

        GlobalScope.launch {
            delay(3000)
            feedbackState = OperationState.Initial
        }
    }

    // Fonction pour mettre à jour un élément
    fun updateElement() {
        if (numeroField.isEmpty()) {
            feedbackState = OperationState.Error("Veuillez entrer un numéro")
            return
        }

        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                when (selectedTab) {
                    StructureType.ALLEE -> {
                        val allee = editingItem as AlleeDetails
                        val query = "UPDATE allee SET Numero = ? WHERE ID_Allee = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.setInt(2, allee.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Allée mise à jour avec succès!")
                    }

                    StructureType.COLONNE -> {
                        val colonne = editingItem as ColonneDetails
                        if (selectedParentId == null) {
                            feedbackState = OperationState.Error("Veuillez sélectionner une allée")
                            return
                        }
                        val query = "UPDATE colonne SET NumeroCol = ?, ID_Allee = ? WHERE ID_Colonne = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.setInt(2, selectedParentId!!)
                            statement.setInt(3, colonne.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Colonne mise à jour avec succès!")
                    }

                    StructureType.ETAGE -> {
                        val etage = editingItem as EtageDetails
                        if (selectedParentId == null) {
                            feedbackState = OperationState.Error("Veuillez sélectionner une colonne")
                            return
                        }
                        val query = "UPDATE etage SET Numero_Etage = ?, ID_Colonne = ? WHERE ID_Etage = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, numeroField.toInt())
                            statement.setInt(2, selectedParentId!!)
                            statement.setInt(3, etage.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Étage mis à jour avec succès!")
                    }
                }

                // Réinitialiser le formulaire
                numeroField = ""
                selectedParentId = null
                editingItem = null
                loadData()
            }
        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la mise à jour: ${e.message}")
        }

        GlobalScope.launch {
            delay(3000)
            feedbackState = OperationState.Initial
        }
    }

    // Fonction pour supprimer un élément
    fun deleteElement(item: Any) {
        try {
            val url = "jdbc:mysql://localhost:3306/carist-si"
            val user = "root"
            val password = ""

            Class.forName("com.mysql.cj.jdbc.Driver")
            DriverManager.getConnection(url, user, password).use { connection ->
                when (selectedTab) {
                    StructureType.ALLEE -> {
                        val allee = item as AlleeDetails
                        val query = "DELETE FROM allee WHERE ID_Allee = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, allee.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Allée supprimée avec succès!")
                    }

                    StructureType.COLONNE -> {
                        val colonne = item as ColonneDetails
                        val query = "DELETE FROM colonne WHERE ID_Colonne = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, colonne.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Colonne supprimée avec succès!")
                    }

                    StructureType.ETAGE -> {
                        val etage = item as EtageDetails
                        val query = "DELETE FROM etage WHERE ID_Etage = ?"
                        connection.prepareStatement(query).use { statement ->
                            statement.setInt(1, etage.id)
                            statement.executeUpdate()
                        }
                        feedbackState = OperationState.Success("Étage supprimé avec succès!")
                    }
                }
                loadData()
            }
        } catch (e: Exception) {
            feedbackState = OperationState.Error("Erreur lors de la suppression: ${e.message}")
        }

        GlobalScope.launch {
            delay(3000)
            feedbackState = OperationState.Initial
        }
    }

    // Charger les données au démarrage
    LaunchedEffect(Unit) {
        loadData()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // En-tête
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestion des Structures",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Button(
                onClick = { onNavigate(Routes.HOME) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
            ) {
                Text("Retour à l'accueil", color = Color.White)
            }
        }

        // Feedback
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

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation par onglets
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            backgroundColor = Color.White,
            contentColor = primaryColor
        ) {
            Tab(
                selected = selectedTab == StructureType.ALLEE,
                onClick = {
                    selectedTab = StructureType.ALLEE
                    isCreating = false
                    editingItem = null
                    numeroField = ""
                    selectedParentId = null
                },
                text = { Text("Allées") }
            )
            Tab(
                selected = selectedTab == StructureType.COLONNE,
                onClick = {
                    selectedTab = StructureType.COLONNE
                    isCreating = false
                    editingItem = null
                    numeroField = ""
                    selectedParentId = null
                },
                text = { Text("Colonnes") }
            )
            Tab(
                selected = selectedTab == StructureType.ETAGE,
                onClick = {
                    selectedTab = StructureType.ETAGE
                    isCreating = false
                    editingItem = null
                    numeroField = ""
                    selectedParentId = null
                },
                text = { Text("Étages") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton d'ajout
        Button(
            onClick = {
                isCreating = !isCreating
                if (!isCreating) {
                    numeroField = ""
                    selectedParentId = null
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = if (isCreating) Icons.Default.Close else Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                if (isCreating) "Annuler" else when (selectedTab) {
                    StructureType.ALLEE -> "Nouvelle Allée"
                    StructureType.COLONNE -> "Nouvelle Colonne"
                    StructureType.ETAGE -> "Nouvel Étage"
                },
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Formulaire de création/édition
        AnimatedVisibility(
            visible = isCreating || editingItem != null,
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (editingItem != null) "Modifier ${when (selectedTab) {
                            StructureType.ALLEE -> "l'allée"
                            StructureType.COLONNE -> "la colonne"
                            StructureType.ETAGE -> "l'étage"
                        }}" else "Créer ${when (selectedTab) {
                            StructureType.ALLEE -> "une nouvelle allée"
                            StructureType.COLONNE -> "une nouvelle colonne"
                            StructureType.ETAGE -> "un nouvel étage"
                        }}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Champ numéro
                    OutlinedTextField(
                        value = numeroField,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                numeroField = newValue
                            }
                        },
                        label = { Text(when (selectedTab) {
                            StructureType.ALLEE -> "Numéro d'allée"
                            StructureType.COLONNE -> "Numéro de colonne"
                            StructureType.ETAGE -> "Numéro d'étage"
                        }) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    // Dropdown pour parent (sauf pour allées)
                    if (selectedTab != StructureType.ALLEE) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = when (selectedTab) {
                                    StructureType.COLONNE -> {
                                        if (selectedParentId != null) {
                                            "Allée ${availableAllees.find { it.id == selectedParentId }?.numero}"
                                        } else "Sélectionner une allée"
                                    }
                                    StructureType.ETAGE -> {
                                        if (selectedParentId != null) {
                                            val colonne = availableColonnes.find { it.id == selectedParentId }
                                            "Colonne ${colonne?.numeroCol} (Allée ${colonne?.numeroAllee})"
                                        } else "Sélectionner une colonne"
                                    }
                                    else -> ""
                                },
                                onValueChange = { },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth().clickable { expandedDropdown = true },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (expandedDropdown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }
                            )

                            DropdownMenu(
                                expanded = expandedDropdown,
                                onDismissRequest = { expandedDropdown = false }
                            ) {
                                when (selectedTab) {
                                    StructureType.COLONNE -> {
                                        availableAllees.forEach { allee ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedParentId = allee.id
                                                    expandedDropdown = false
                                                }
                                            ) {
                                                Text("Allée ${allee.numero}")
                                            }
                                        }
                                    }
                                    StructureType.ETAGE -> {
                                        availableColonnes.forEach { colonne ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedParentId = colonne.id
                                                    expandedDropdown = false
                                                }
                                            ) {
                                                Text("Colonne ${colonne.numeroCol} (Allée ${colonne.numeroAllee})")
                                            }
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }

                    // Boutons d'action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        Button(
                            onClick = {
                                numeroField = ""
                                selectedParentId = null
                                isCreating = false
                                editingItem = null
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                        ) {
                            Text("Annuler", color = Color.White)
                        }

                        Button(
                            onClick = {
                                if (editingItem != null) {
                                    updateElement()
                                } else {
                                    createElement()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
                        ) {
                            Text(
                                if (editingItem != null) "Mettre à jour" else "Créer",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Liste des éléments
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (selectedTab) {
                StructureType.ALLEE -> {
                    items(allees) { allee ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "Allée ${allee.numero}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "ID: ${allee.id}",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            editingItem = allee
                                            numeroField = allee.numero.toString()
                                            isCreating = false
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                                    }

                                    IconButton(
                                        onClick = { deleteElement(allee) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }

                StructureType.COLONNE -> {
                    items(colonnes) { colonne ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "Colonne ${colonne.numeroCol}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Allée ${colonne.numeroAllee} | ID: ${colonne.id}",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            editingItem = colonne
                                            numeroField = colonne.numeroCol.toString()
                                            selectedParentId = colonne.idAllee
                                            isCreating = false
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                                    }

                                    IconButton(
                                        onClick = { deleteElement(colonne) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }

                StructureType.ETAGE -> {
                    items(etages) { etage ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "Étage ${etage.numeroEtage}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "Colonne ${etage.numeroColonne} - Allée ${etage.numeroAllee} | ID: ${etage.id}",
                                        color = Color.Gray,
                                        fontSize = 12.sp
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            editingItem = etage
                                            numeroField = etage.numeroEtage.toString()
                                            selectedParentId = etage.idColonne
                                            isCreating = false
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                                    }

                                    IconButton(
                                        onClick = { deleteElement(etage) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}