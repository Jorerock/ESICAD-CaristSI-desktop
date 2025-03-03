package ui
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.LoginScreen
import routing.Router
import routing.Routes
import ui.HomeScreen
import ui.LoginScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import ktorm.Caristes
import ktorm.Caristes.primaryKey
import ktorm.colis
import ktorm.place
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


import java.sql.DriverManager
import java.time.LocalDate


@Composable
fun CreateColis(database : Database, onNavigate:(Routes)->Unit) {
//    var ID by remember { mutableStateOf("") }
    var Longueur by remember { mutableStateOf("") }
    var Largeur by remember { mutableStateOf("")}
    var Hauteur by remember { mutableStateOf("")}
    var Poids by remember { mutableStateOf("")}
    var ID_Allee  by remember { mutableStateOf("")}
    var Numero by remember { mutableStateOf("")}
    var ID_Colonne by remember { mutableStateOf("")}
    var NumeroCol by remember { mutableStateOf("")}
    var ID_Etage by remember { mutableStateOf("")}
    var Numero_Etage by remember { mutableStateOf("")}
    var ID_Emplacement by remember { mutableStateOf("")}
    var VolumeMax by remember { mutableStateOf("")}
    var PoidsMax by remember { mutableStateOf("")}
    var errorMessage by remember { mutableStateOf<String?>(null) }



    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row {
            Button(
                onClick = {
                    onNavigate(Routes.RECHERCHE)
                },
                modifier = Modifier.weight(2f).padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            Button(
                onClick = {
                    val updatedColis = Colliss(
                        ID = 0,
                        Longueur = Longueur.toInt(),
                        Largeur = Largeur.toInt() ,
                        Hauteur = Hauteur.toInt(),
                        Poids = Poids.toInt(),
                        ID_Allee = 0,
                        Numero = Numero.toInt(),
                        ID_Colonne = 0,
                        NumeroCol = NumeroCol.toInt(),
                        ID_Etage = 0,
                        Numero_Etage = Numero_Etage.toInt(),
                        ID_Emplacement = 0,
                        VolumeMax = 0,
                        PoidsMax = 0,
                    )
                    insertColis(database, updatedColis,SelectEmplacement(updatedColis))
                },

                modifier = Modifier.weight(2f).padding(vertical = 8.dp)
            ) {
                Text("Cree le colis`")
            }
            }
            Text("Creation Cariste", fontSize = 24.sp)

            OutlinedTextField(
                value = Longueur,
                onValueChange = { Longueur = it },
                label = { Text("Longueur") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = Largeur,
                onValueChange = { Largeur = it },
                label = { Text("Largeur") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = Hauteur,
                onValueChange = { Hauteur = it },
                label = { Text("Hauteur") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = Poids,
                onValueChange = { Poids = it },
                label = { Text("Poids") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Row {
                OutlinedTextField(
                    value = Numero ,
                    onValueChange = { newValue ->
                        // Vous pouvez ajouter une validation ici si nécessaire
                        Numero  = newValue
                        // Pour mettre à jour la valeur dans votre modèle:
                        // Convertir en Int avec gestion d'erreur
                        newValue.toIntOrNull()?.let { numCol ->
                            Numero  = numCol.toString()
                        }
                    },
                    label = { Text("Numero Allee") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = NumeroCol  ,
                    onValueChange = { newValue ->
                        // Vous pouvez ajouter une validation ici si nécessaire
                        NumeroCol   = newValue
                        // Pour mettre à jour la valeur dans votre modèle:
                        // Convertir en Int avec gestion d'erreur
                        newValue.toIntOrNull()?.let { numCol ->
                            NumeroCol   = numCol.toString()
                        }
                    },
                    label = { Text("NumeroCol ") },
                    modifier = Modifier.weight(2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = Numero_Etage   ,
                    onValueChange = { newValue ->
                        // Vous pouvez ajouter une validation ici si nécessaire
                        Numero_Etage    = newValue
                        // Pour mettre à jour la valeur dans votre modèle:
                        // Convertir en Int avec gestion d'erreur
                        newValue.toIntOrNull()?.let { numCol ->
                            Numero_Etage    = numCol.toString()
                        }
                    },
                    label = { Text("Numero_Etage") },
                    modifier = Modifier.weight(3f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }


            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }

        }
    }
}


data class newColis(
    var Longueur :Int,
    var Largeur:Int,
    var Hauteur :Int,
    var Poids :Int,
)

fun insertColis(database : Database, Newcolis : Colliss, Emplacement : Int): String {

    try {

        println("update colis"+ colis)
        var NewID = database.insertAndGenerateKey(ktorm.colis) {
            set(colis.Longueur, Newcolis.Longueur)
            set(colis.Largeur, Newcolis.Largeur)
            set(colis.Hauteur, Newcolis.Hauteur)
            set(colis.Poids, Newcolis.Poids)
        } as Int
        println(NewID)

        database.insert(ktorm.place) {
            set(place.ID_cariste, 1)
            set(place.ID_Colis, NewID)
            set(place.ID_Emplacement, Emplacement)
            set(place.DateDepose, LocalDate.now())
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}