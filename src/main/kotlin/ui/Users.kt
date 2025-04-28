package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import ktorm.Caristes
import org.ktorm.database.Database
import org.ktorm.dsl.*
import routing.Routes
import ui.Element.FeedbackMessage
import ui.Element.OperationState
import ui.Element.primaryColor
import java.sql.Date
import java.sql.DriverManager

@Composable
fun CaristesScreen(database: Database, onNavigate: (Routes) -> Unit) {
    var caristes by remember { mutableStateOf<List<Cariste>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var feedbackState by remember { mutableStateOf<OperationState<String>>(OperationState.Initial) }
    var showDeleteConfirmDialog by remember { mutableStateOf<Cariste?>(null) }
    var selectedCariste by remember { mutableStateOf<Cariste?>(null) }
    var showDetailsDialog by remember { mutableStateOf(false) }

    // Charger les données au démarrage
    LaunchedEffect(Unit) {
        caristes = requestUsers()
    }

    // Filtrer les caristes en fonction de la recherche
    val filteredCaristes = caristes.filter {
        it.nom.contains(searchQuery, ignoreCase = true) ||
                it.prenom.contains(searchQuery, ignoreCase = true) ||
                it.ID_Cariste.toString().contains(searchQuery)
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
                text = "Gestion des Caristes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            // En-tête avec titre et boutons d'action
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gestion des Caristes",
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
                        onClick = { onNavigate(Routes.NEWCARISTES) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
                    ) {
                        Text("Ajouter un cariste")
                    }
                }
            }
//
//            Row(spacing = 8.dp) {
//                Button(
//                    onClick = { onNavigate(Routes.HOME) },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
//                ) {
//                    Text("Retour")
//                }
//
//                Button(
//                    onClick = { onNavigate(Routes.NEWCARISTES) },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
//                ) {
//                    Text("Ajouter un cariste")
//                }
//            }
        }

        // Barre de recherche
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Rechercher un cariste") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Text("✕")
                    }
                }
            }
        )

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

        // En-tête du tableau
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryColor.copy(alpha = 0.1f))
                .padding(8.dp)
        ) {
            Text("ID", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            Text("Nom", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
            Text("Prénom", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
            Text("Actions", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }

        // Liste des caristes
        if (filteredCaristes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isEmpty()) "Aucun cariste trouvé" else "Aucun résultat pour '$searchQuery'",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(filteredCaristes) { cariste ->
                    CaristeRow(
                        cariste = cariste,
                        onEdit = { updatedCariste ->
                            UpdateUser(database, updatedCariste)
                            // Rafraîchir la liste
                            caristes = requestUsers()
                            feedbackState = OperationState.Success("Cariste mis à jour avec succès")

                            // Réinitialiser l'état après 3 secondes
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(3000)
                                feedbackState = OperationState.Initial
                            }
                        },
                        onDelete = {
                            showDeleteConfirmDialog = cariste
                        },
                        onDetails = {
                            selectedCariste = cariste
                            showDetailsDialog = true
                        }
                    )
                }
            }
        }

        // Afficher nombre de caristes
        Text(
            text = "${filteredCaristes.size} cariste(s) affiché(s) sur ${caristes.size} total",
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }

    // Boîte de dialogue de confirmation de suppression
    if (showDeleteConfirmDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text("Confirmer la suppression") },
            text = { Text("Êtes-vous sûr de vouloir supprimer le cariste ${showDeleteConfirmDialog?.prenom} ${showDeleteConfirmDialog?.nom} ?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Logique de suppression ici
                        deleteCariste(database, showDeleteConfirmDialog!!)
                        caristes = requestUsers()
                        feedbackState = OperationState.Success("Cariste supprimé avec succès")
                        showDeleteConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Supprimer", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Boîte de dialogue de détails
    if (showDetailsDialog && selectedCariste != null) {
        Dialog(onDismissRequest = { showDetailsDialog = false }) {
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Détails du cariste",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("ID: ", fontWeight = FontWeight.Bold)
                        Text(selectedCariste!!.ID_Cariste.toString())
                    }

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Nom: ", fontWeight = FontWeight.Bold)
                        Text(selectedCariste!!.nom)
                    }

                    Row(modifier = Modifier.padding(vertical = 4.dp)) {
                        Text("Prénom: ", fontWeight = FontWeight.Bold)
                        Text(selectedCariste!!.prenom)
                    }

                    // Vous pourriez ajouter plus de détails ici si vous avez d'autres champs

                    Button(
                        onClick = { showDetailsDialog = false },
                        modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
                    ) {
                        Text("Fermer")
                    }
                }
            }
        }
    }
}

@Composable
fun CaristeRow(
    cariste: Cariste,
    onEdit: (Cariste) -> Unit,
    onDelete: () -> Unit,
    onDetails: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var nom by remember { mutableStateOf(cariste.nom) }
    var prenom by remember { mutableStateOf(cariste.prenom) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ID
            Text(
                text = cariste.ID_Cariste.toString(),
                modifier = Modifier.weight(0.5f)
            )

            // Nom
            if (isEditing) {
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    modifier = Modifier.weight(1.5f).padding(horizontal = 4.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = cariste.nom,
                    modifier = Modifier.weight(1.5f)
                )
            }

            // Prénom
            if (isEditing) {
                OutlinedTextField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("Prénom") },
                    modifier = Modifier.weight(1.5f).padding(horizontal = 4.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = cariste.prenom,
                    modifier = Modifier.weight(1.5f)
                )
            }

            // Actions
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (isEditing) {
                    IconButton(onClick = {
                        val updatedCariste = Cariste(
                            ID_Cariste = cariste.ID_Cariste,
                            nom = nom,
                            prenom = prenom
                        )
                        onEdit(updatedCariste)
                        isEditing = false
                    }) {
                        Text("✓")  // Symbole de validation
                    }

                    IconButton(onClick = {
                        isEditing = false
                        nom = cariste.nom
                        prenom = cariste.prenom
                    }) {
                        Text("✕")  // Symbole d'annulation
                    }
                } else {
                    Button(
                        onClick = { isEditing = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
                    ) {
                        Text("Modifier")
                    }

                    Button(
                        onClick = onDetails,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                    ) {
                        Text("Détails")
                    }

                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}

fun deleteCariste(database: Database, cariste: Cariste) {
    try {
        database.delete(Caristes) {
            it.ID_cariste eq cariste.ID_Cariste
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Gardez les fonctions existantes
fun requestUsers(): List<Cariste> {
    val users = mutableListOf<Cariste>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""

    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            val query = "SELECT * FROM caristes"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    users.add(
                        Cariste(
                            ID_Cariste = resultSet.getInt("ID_Cariste"),
                            nom = resultSet.getString("Nom"),
                            prenom = resultSet.getString("prenom")
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return users
}

data class Cariste(
    val ID_Cariste: Int,
    val nom: String,
    val prenom: String
)

fun UpdateUser(database: Database, cariste: Cariste): String {
    try {
        println("update cariste" + cariste)
        database.update(Caristes) {
            set(Caristes.nom, cariste.nom)
            set(Caristes.prenom, cariste.prenom)
            where {
                Caristes.ID_cariste eq cariste.ID_Cariste
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}

// Extension pour simplifier les Row
//fun RowScope.spacing(space: Int) = Spacer(modifier = Modifier.width(space.dp))