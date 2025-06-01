//package ui
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.ktorm.database.Database
//import org.ktorm.dsl.*
//import routing.Routes
//import ui.Element.FeedbackMessage
//import ui.Element.OperationState
//import ui.Element.primaryColor
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.text.input.TextFieldValue
//import java.sql.DriverManager
//import kotlin.use
//import ktorm.colis
//
//@Composable
//fun RechercheEtInfoColis(database: Database, onNavigate: (Routes) -> Unit) {
//    // État des données de recherche
//    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
//    var colisFound by remember { mutableStateOf(false) }
//
//    // États pour les détails du colis
//    var currentColis by remember { mutableStateOf<ColisDetails?>(null) }
//    var longueur by remember { mutableStateOf("") }
//    var largeur by remember { mutableStateOf("") }
//    var hauteur by remember { mutableStateOf("") }
//    var poids by remember { mutableStateOf("") }
//
//    // État pour les messages d'erreur ou de succès
//    var feedbackMessage by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        // Barre de titre avec bouton retour si un colis est affiché
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            if (colisFound) {
//                IconButton(onClick = {
//                    colisFound = false
//                    currentColis = null
//                    feedbackMessage = null
//                }) {
//                    Text("←", fontSize = 24.sp)
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//            }
//
//            Text(
//                if (!colisFound) "Recherche de colis" else "Détails du colis #${currentColis?.id}",
//                style = MaterialTheme.typography.h6
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Contenu principal
//        if (!colisFound) {
//            // Interface de recherche
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                OutlinedTextField(
//                    value = searchQuery,
//                    onValueChange = { searchQuery = it },
//                    label = { Text("ID du colis") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        val idColis = searchQuery.text.toIntOrNull()
//                        if (idColis != null) {
//                            isLoading = true
//                            try {
//                                println("Recherche de ID_Colis $idColis")
//
//                                // Récupérer les détails du colis depuis la base de données
//                                val result = database.from(colis)
//                                    .select()
//                                    .where { colis.ID eq idColis }
//                                    .map { row ->
//                                        ColisDetails(
//                                            id = row[colis.ID]!!,
//                                            longueur = row[colis.Longueur]!!,
//                                            largeur = row[colis.Largeur]!!,
//                                            hauteur = row[colis.Hauteur]!!,
//                                            poids = row[colis.Poids]!!
//                                        )
//                                    }
//                                    .firstOrNull()
//
//                                // Transférer les résultats vers mockColis et les variables d'état
//                                val mockColis = result
//
//                                if (mockColis != null) {
//                                    currentColis = mockColis
//                                    longueur = mockColis.longueur.toString()
//                                    largeur = mockColis.largeur.toString()
//                                    hauteur = mockColis.hauteur.toString()
//                                    poids = mockColis.poids.toString()
//                                    colisFound = true
//                                    feedbackMessage = Pair("Colis trouvé avec succès", false)
//                                } else {
//                                    feedbackMessage = Pair("Colis avec ID $idColis non trouvé", true)
//                                }
//                            } catch (e: Exception) {
//                                feedbackMessage = Pair("Erreur lors de la recherche: ${e.localizedMessage ?: "Erreur inconnue"}", true)
//                                println("Erreur lors de la recherche: ${e.message}")
//                            } finally {
//                                isLoading = false
//                            }
//                        } else {
//                            feedbackMessage = Pair("Veuillez entrer un ID valide", true)
//                        }
//                    }
//                ) {
//                    Text("Rechercher")
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Afficher le feedback (erreur ou succès)
//                feedbackMessage?.let { (message, isError) ->
//                    Text(
//                        text = message,
//                        color = if (isError) MaterialTheme.colors.error else Color.Green,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//
//                if (isLoading) {
//                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Navigation vers l'accueil
//                Button(
//                    onClick = { onNavigate(Routes.HOME) },
//                    modifier = Modifier.align(Alignment.Start)
//                ) {
//                    Text("Retour à l'accueil")
//                }
//            }
//        } else {
//            // Affichage des détails du colis
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = 4.dp
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    // Informations du colis
//                    Text(
//                        "Dimensions et poids",
//                        style = MaterialTheme.typography.subtitle1,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    // Champs pour modifier les détails
//                    OutlinedTextField(
//                        value = longueur,
//                        onValueChange = { longueur = it },
//                        label = { Text("Longueur") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//                    OutlinedTextField(
//                        value = largeur,
//                        onValueChange = { largeur = it },
//                        label = { Text("Largeur") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//                    OutlinedTextField(
//                        value = hauteur,
//                        onValueChange = { hauteur = it },
//                        label = { Text("Hauteur") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//                    OutlinedTextField(
//                        value = poids,
//                        onValueChange = { poids = it },
//                        label = { Text("Poids") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Actions sur le colis
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Button(
//                            onClick = {
//                                try {
//                                    // Mettre à jour les détails du colis
//                                    val updatedColis = currentColis?.copy(
//                                        longueur = longueur.toIntOrNull() ?: currentColis!!.longueur,
//                                        largeur = largeur.toIntOrNull() ?: currentColis!!.largeur,
//                                        hauteur = hauteur.toIntOrNull() ?: currentColis!!.hauteur,
//                                        poids = poids.toIntOrNull() ?: currentColis!!.poids
//                                    )
//
//                                    if (updatedColis != null) {
//                                        // Mettre à jour dans la base de données
//                                        database.update(colis) {
//                                            set(colis.Longueur, updatedColis.longueur)
//                                            set(colis.Largeur, updatedColis.largeur)
//                                            set(colis.Hauteur, updatedColis.hauteur)
//                                            set(colis.Poids, updatedColis.poids)
//                                            where {
//                                                colis.ID eq updatedColis.id
//                                            }
//                                        }
//
//                                        // Mettre à jour l'état local
//                                        currentColis = updatedColis
//                                        feedbackMessage = Pair("Colis mis à jour avec succès", false)
//                                    }
//                                } catch (e: Exception) {
//                                    feedbackMessage = Pair("Erreur lors de la mise à jour: ${e.localizedMessage ?: "Erreur inconnue"}", true)
//                                }
//                            }
//                        ) {
//                            Text("Mettre à jour")
//                        }
//
//                        Button(
//                            onClick = {
//                                try {
//                                    // Supprimer le colis
//                                    currentColis?.let { colisToDelete ->
//                                        database.delete(colis) {
//                                            it.ID eq colisToDelete.id
//                                        }
//
//                                        feedbackMessage = Pair("Colis supprimé avec succès", false)
//                                        colisFound = false
//                                        currentColis = null
//                                    }
//                                } catch (e: Exception) {
//                                    feedbackMessage = Pair("Erreur lors de la suppression: ${e.localizedMessage ?: "Erreur inconnue"}", true)
//                                }
//                            },
//                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
//                        ) {
//                            Text("Supprimer")
//                        }
//                    }
//
//                    // Afficher les messages de feedback
//                    feedbackMessage?.let { (message, isError) ->
//                        Text(
//                            text = message,
//                            color = if (isError) MaterialTheme.colors.error else Color.Green,
//                            modifier = Modifier.padding(vertical = 16.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//// Classe pour stocker les détails d'un colis
//data class ColisDetails(
//    val id: Int,
//    val longueur: Int,
//    val largeur: Int,
//    val hauteur: Int,
//    val poids: Int
//)
//
