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
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun CreateCariste(database: Database, onNavigate: (Routes) -> Unit) {
    var nom by remember { mutableStateOf(TextFieldValue("")) }
    var prenom by remember { mutableStateOf(TextFieldValue("")) }
    var mdp by remember { mutableStateOf(TextFieldValue("")) }
    var login by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var feedbackState by remember { mutableStateOf<OperationState<String>>(OperationState.Initial) }

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
                text = "Création de Cariste",
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
                    onClick = { onNavigate(Routes.CARISTES) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text("Liste des caristes")
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

        // Carte principale du formulaire
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Champ Nom
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Champ Prénom
                OutlinedTextField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("Prénom") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Champ Login
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Identifiant") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Champ Mot de passe
                OutlinedTextField(
                    value = mdp,
                    onValueChange = { mdp = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Message d'erreur éventuel
                errorMessage?.let {
                    Text(it, color = MaterialTheme.colors.error)
                }

                // Bouton de validation
                Button(
                    onClick = {
                        if (nom.text.isBlank() || prenom.text.isBlank() || login.text.isBlank() || mdp.text.isBlank()) {
                            errorMessage = "Tous les champs sont obligatoires"
                        } else {
                            val updatedcariste = newCariste(
                                nom = nom.text,
                                prenom = prenom.text,
                                login = login.text,
                                mdp = mdp.text
                            )
                            try {
                                insert(database, updatedcariste)
                                feedbackState = OperationState.Success("Cariste créé avec succès")
                                // Réinitialiser les champs
                                nom = TextFieldValue("")
                                prenom = TextFieldValue("")
                                login = TextFieldValue("")
                                mdp = TextFieldValue("")
                                errorMessage = null

                                // Réinitialiser le feedback après 3 secondes
                                kotlinx.coroutines.GlobalScope.launch {
                                    kotlinx.coroutines.delay(3000)
                                    feedbackState = OperationState.Initial
                                }
                            } catch (e: Exception) {
                                feedbackState = OperationState.Error("Erreur lors de la création: ${e.message}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                ) {
                    Text("Créer le cariste", color = Color.White)
                }
            }
        }
    }
}

// Ajoutez cette classe de données dans le même fichier que CreateCariste
data class newCariste(
    val nom: String,
    val prenom: String,
    val mdp: String,
    val login: String
)

// Et assurez-vous que la fonction insert() est également présente
fun insert(database: Database, cariste: newCariste): String {
    try {
        println("update cariste" + cariste)
        database.insert(Caristes) {
            set(Caristes.nom, cariste.nom)
            set(Caristes.prenom, cariste.prenom)
            set(Caristes.mdp, cariste.mdp)
            set(Caristes.login, cariste.login)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}