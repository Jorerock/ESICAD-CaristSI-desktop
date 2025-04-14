//package ui
//
//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//import ktorm.Colonne
//import ktorm.Emplacement
//import ktorm.entity.AlleeEntity
//import ktorm.entity.ColonneEntity
//import ktorm.entity.EmplacementEntity
//import org.ktorm.database.Database
//import org.ktorm.dsl.eq
//import org.ktorm.dsl.insert
//import org.ktorm.entity.filter
//import org.ktorm.entity.sequenceOf
//import org.ktorm.entity.toList
//import routing.Routes
//
//@Composable
//@Preview
//fun AlleeScreen(database: Database, onNavigate: (Routes) -> Unit) {
//    var allees by remember { mutableStateOf(emptyList<ktorm.allee>()) }
//    var selectedAllee by remember { mutableStateOf<allee?>(null) }
//    var colonnes by remember { mutableStateOf(emptyList<ColonneEntity>()) }
//    var selectedColonne by remember { mutableStateOf<ColonneEntity?>(null) }
//    var emplacements by remember { mutableStateOf(emptyList<EmplacementEntity>()) }
//    var isMenuExpanded by remember { mutableStateOf(false) }
//    var isColonneMenuExpanded by remember { mutableStateOf(false) }
//    var showCreateEmplacementDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        allees = requeteAllees(database)
//    }
//
////    se lance lorsque selectedAllee est modifier
//    LaunchedEffect(selectedAllee) {
//        selectedAllee?.let {
//            colonnes = requeteColonnes(database, it.ID_Allee)
//            selectedColonne = null // Reset selectedColonne when allee changes
//        }
//    }
//
//    LaunchedEffect(selectedColonne) {
//        selectedColonne?.let {
//            emplacements = requeteEmplacements(database, it.ID_Colonne)
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)) {
//            Text("Gestion des Allées", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))
//
//            // Sélection de l'allée
//            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth().clickable { isMenuExpanded = true },
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(selectedAllee?.Numero?.toString() ?: "Sélectionnez une allée")
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//                }
//                DropdownMenu(
//                    expanded = isMenuExpanded,
//                    onDismissRequest = { isMenuExpanded = false },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    allees.forEach { allee ->
//                        DropdownMenuItem(onClick = {
//                            selectedAllee = allee
//                            isMenuExpanded = false
//                        }) {
//                            Text(allee.Numero.toString())
//                        }
//                    }
//                }
//            }
//
//            // Sélection de la colonne
//            selectedAllee?.let {
//                Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth().clickable { isColonneMenuExpanded = true },
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(selectedColonne?.NumeroCol?.toString() ?: "Sélectionnez une colonne")
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//                    }
//                    DropdownMenu(
//                        expanded = isColonneMenuExpanded,
//                        onDismissRequest = { isColonneMenuExpanded = false },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        colonnes.forEach { colonne ->
//                            DropdownMenuItem(onClick = {
//                                selectedColonne = colonne
//                                isColonneMenuExpanded = false
//                            }) {
//                                Text(colonne.NumeroCol.toString())
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Affichage des emplacements
//            selectedColonne?.let {
//                EmplacementsTable(emplacements = emplacements)
//            }
//
//            // Bouton pour créer un nouvel emplacement
//            Button(
//                onClick = { showCreateEmplacementDialog = true },
//                enabled = selectedAllee != null && selectedColonne != null,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .alpha(if (selectedAllee != null && selectedColonne != null) 1f else 0.5f)
//            ) {
//                Text("Créer un nouvel emplacement")
//            }
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .padding(16.dp)
//        ) {
//            Button(onClick = { onNavigate(Routes.HOME) }) {
//                Text("Retour accueil")
//            }
//        }
//    }
//
//    if (showCreateEmplacementDialog) {
//        CreateEmplacementDialog(
//            database = database,
//            selectedColonne = selectedColonne,
//            onDismiss = { showCreateEmplacementDialog = false },
//            onEmplacementCreated = {
//                emplacements = requeteEmplacements(database, selectedColonne!!.ID_Colonne)
//            }
//        )
//    }
//}
//
//@Composable
//fun CreateEmplacementDialog(
//    database: Database,
//    selectedColonne: ColonneEntity?,
//    onDismiss: () -> Unit,
//    onEmplacementCreated: () -> Unit
//) {
//    var volumeMax by remember { mutableStateOf("") }
//    var poidsMax by remember { mutableStateOf("") }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    AlertDialog(
//        onDismissRequest = { onDismiss() },
//        title = { Text("Créer un nouvel emplacement") },
//        text = {
//            Column {
//                OutlinedTextField(
//                    singleLine = true,
//                    value = volumeMax,
//                    onValueChange = { volumeMax = it },
//                    label = { Text("Volume Max") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                OutlinedTextField(
//                    singleLine = true,
//                    value = poidsMax,
//                    onValueChange = { poidsMax = it },
//                    label = { Text("Poids Max") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                errorMessage?.let {
//                    Text(it, color = Color.Red)
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                errorMessage = null
//                try {
//                    selectedColonne?.let { colonne ->
//                        createEmplacement(database, colonne.ID_Colonne, volumeMax.toInt(), poidsMax.toInt())
//                        onEmplacementCreated()
//                        onDismiss()
//                    }
//                } catch (e: NumberFormatException) {
//                    errorMessage = "Veuillez entrer des valeurs numériques valides."
//                }
//            }) {
//                Text("Créer")
//            }
//        },
//        dismissButton = {
//            Button(onClick = { onDismiss() }) {
//                Text("Annuler")
//            }
//        }
//    )
//}
//
//@Composable
//fun EmplacementsTable(emplacements: List<EmplacementEntity>) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        elevation = 4.dp
//    ) {
//        Column {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    "ID Emplacement",
//                    modifier = Modifier.weight(1f),
//                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                )
//                Text(
//                    "Volume Max",
//                    modifier = Modifier.weight(1f),
//                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                )
//                Text(
//                    "Poids Max",
//                    modifier = Modifier.weight(1f),
//                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                )
//            }
//            Divider()
//
//            LazyColumn {
//                items(emplacements) { emplacement ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(emplacement.ID_Emplacement.toString(), modifier = Modifier.weight(1f))
//                        Text(emplacement.VolumeMax.toString(), modifier = Modifier.weight(1f))
//                        Text(emplacement.PoidsMax.toString(), modifier = Modifier.weight(1f))
//                    }
//                }
//            }
//        }
//    }
//}
//
//fun createEmplacement(database: Database, colonneId: Int, volumeMax: Int, poidsMax: Int) {
//    database.insert(Emplacement) {
//        set(it.VolumeMax, volumeMax)
//        set(it.PoidsMax, poidsMax)
//        set(it.ID_Colonne, colonneId)
//    }
//}
//
//fun requeteAllees(database: Database): List<AlleeEntity> {
//    return try {
//        database.sequenceOf(Allees).toList()
//    } catch (e: Exception) {
//        println("Erreur lors de la récupération des données : ${e.message}")
//        emptyList()
//    }
//}
//
//fun requeteColonnes(database: Database, alleeId: Int): List<ColonneEntity> {
//    return try {
//        database.sequenceOf(Colonne).filter { it.ID_Allee eq alleeId }.toList()
//    } catch (e: Exception) {
//        println("Erreur lors de la récupération des colonnes : ${e.message}")
//        emptyList()
//    }
//}
//
//fun requeteEmplacements(database: Database, colonneId: Int): List<EmplacementEntity> {
//    return try {
//        database.sequenceOf(Emplacement).filter { it.ID_Colonne eq colonneId }.toList()
//    } catch (e: Exception) {
//        println("Erreur lors de la récupération des emplacements : ${e.message}")
//        emptyList()
//    }
//}
