
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
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


import java.sql.DriverManager
import java.time.LocalDate


@Composable
fun CreateCariste(database : Database, onNavigate:(Routes)->Unit) {
    var nom by remember { mutableStateOf(TextFieldValue("")) }
    var prenom by remember { mutableStateOf(TextFieldValue("")) }
    var mdp by remember { mutableStateOf(TextFieldValue("")) }
    var naissance by remember { mutableStateOf(TextFieldValue("")) }

    var embauche by remember { mutableStateOf(TextFieldValue("")) }
    var login by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }



    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){
        var Alee  = LocalDate.now()

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            //SimpleDatePicker(LocalDate.now(),Alee)
            Button(
                onClick = {
                    onNavigate(Routes.CARISTES)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            Text("Creation Cariste", fontSize = 24.sp)

            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("nom") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("prenom") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("login") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mdp,
                onValueChange = { mdp = it },
                label = { Text("mdp") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }


                    Button(
                        onClick = {
                            val updatedcariste = newCariste(
                                nom = nom.text,
                                prenom = prenom.text,
                               login = login.text,
                                mdp = mdp.text
                            )
                            insert(database, updatedcariste)
                        },

                       modifier = Modifier.weight(4f).padding(vertical = 8.dp)
                   ) {
                        Text("Cree l'utilisateur`")

                    }


        }
    }
}


data class newCariste(
    val nom: String,
    val prenom: String,
    val mdp: String,
    val login: String

)

fun insert(database : Database, cariste : newCariste): String {

    try {

        println("update cariste"+ cariste)
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