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
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


import java.sql.DriverManager



@Composable
fun CreateColis(database : Database, onNavigate:(Routes)->Unit) {
//    var ID by remember { mutableStateOf("") }
    var Longueur by remember { mutableStateOf("") }
    var Largeur by remember { mutableStateOf("")}
    var Hauteur by remember { mutableStateOf("")}
    var Poids by remember { mutableStateOf("")}

    var errorMessage by remember { mutableStateOf<String?>(null) }



    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onNavigate(Routes.RECHERCHE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
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

            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }


            Button(
                onClick = {
                    val updatedColis = newColis(
                        Longueur = Longueur.toInt(),
                        Largeur = Largeur.toInt(),
                        Hauteur = Hauteur.toInt(),
                        Poids = Poids.toInt()
                    )
                    insertColis(database, updatedColis)
                },

                modifier = Modifier.weight(4f).padding(vertical = 8.dp)
            ) {
                Text("Cree le colis`")

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

fun insertColis(database : Database, Newcolis : newColis): String {

    try {

        println("update colis"+ colis)
        database.insert(ktorm.colis) {
            set(colis.Longueur, Newcolis.Longueur)
            set(colis.Largeur, Newcolis.Largeur)
            set(colis.Hauteur, Newcolis.Hauteur)
            set(colis.Poids, Newcolis.Poids)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}