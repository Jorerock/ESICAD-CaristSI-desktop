
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
import ktorm.colis
import org.ktorm.database.Database
import org.ktorm.dsl.*

import java.sql.DriverManager



@Composable
fun AlleeScreen(database : Database, onNavigate:(Routes)->Unit) {
    val allees = requestAllee()
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){

        Column {
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
                    onNavigate(Routes.CREATEALLEE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Créer une nouvelle allee")
            }

            Text("Choisir une Allee :")

                allees.forEach { allee ->
                    Row {

                        Button(
                            onClick = {
                                onNavigate(Routes.COLONNE)
                            },
                            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
                        ) {
                            Text(allee.Numero.toString())
                        }

                        Button(
                            onClick = {
                                deleteAllee(database,allee)
                            },
                            modifier = Modifier.weight(3f).padding(vertical = 8.dp)
                        ) {
                            Text("Supprimer")
                        }
                    }
                }

        }
    }
}


fun requestAllee(): List<allee> {
    val allees = mutableListOf<allee>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            val query = "select * from allee"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    allees.add(
                        allee(
                            ID_Allee = resultSet.getInt("allee.ID_Allee"),
                            Numero = resultSet.getInt("allee.Numero"),

                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return allees
}

data class allee(
    val ID_Allee: Int,
    val Numero: Int,

)



fun deleteAllee(database : Database, Allee : allee): String {

    try {
        database.delete(ktorm.allee)
             {
                ktorm.allee.ID_Allee eq Allee.ID_Allee
            }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}

