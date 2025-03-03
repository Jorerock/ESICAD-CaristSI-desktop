
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
import androidx.compose.ui.text.input.TextFieldValue
import ktorm.Caristes
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.sql.Date


import java.sql.DriverManager



@Composable
fun CaristesScreen(database : Database, onNavigate:(Routes)->Unit) {
    val caristes = requestUsers()


    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Button(
                onClick = {
                    onNavigate(Routes.HOME)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }
            Button(
                onClick = {
                    onNavigate(Routes.NEWCARISTES)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Créer un nouveau cariste")
            }
            // Table Header
            Row {
                Text("id", modifier = Modifier.weight(1f))
                Text("Nom", modifier = Modifier.weight(2f))
                Text("Prenom", modifier = Modifier.weight(3f))
//                Text("Login", modifier = Modifier.weight(4f))
//                Text("Embauche", modifier = Modifier.weight(6f))
//                Text("Mot de Passe", modifier = Modifier.weight(5f))
//                Text("Naissance", modifier = Modifier.weight(7f))
                Text("Action", modifier = Modifier.weight(4f))


            }
            // Table Rows
            caristes.forEach { user ->
                var Caristenom by remember { mutableStateOf(TextFieldValue(user.nom)) }
                var CaristePrenom by remember { mutableStateOf(TextFieldValue(user.prenom)) }
//                var Login by remember { mutableStateOf(TextFieldValue(user.Login)) }
//                var MDP by remember { mutableStateOf(TextFieldValue(user.MDP)) }
//                var Naissance by remember { mutableStateOf(TextFieldValue(user.Naissance)) }
//                var Embauche by remember { mutableStateOf(TextFieldValue(user.Embauche)) }
                var isEditMode by remember { mutableStateOf(false) }

                Row {
                    Text(user.ID_Cariste .toString(), modifier = Modifier.weight(1f))

                    OutlinedTextField(
                        value = Caristenom,
                        onValueChange = { Caristenom = it },
                        label = { Caristenom },
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = CaristePrenom,
                        onValueChange = { CaristePrenom = it },
                        label = { CaristePrenom },
                        modifier = Modifier.weight(3f)
                    )

                    Button(
                        onClick = {
                            val updatedcariste = Cariste(
                                ID_Cariste  = user.ID_Cariste,
                                nom = Caristenom.text,
                                prenom = CaristePrenom.text
                            )
                            UpdateUser(database, updatedcariste)
                        },

                        modifier = Modifier.weight(4f).padding(vertical = 8.dp)
                    ) {
                        Text("Modifier")

                    }
                }
            }
        }
    }
}

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
                            ID_Cariste  = resultSet.getInt("ID_Cariste"),
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
//    val Naissance: Date
//    val Embauche: Date
//    val MDP: String
//    val Login: String
)




fun UpdateUser(database : Database, cariste : Cariste): String {

    try {

        println("update cariste"+ cariste)
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