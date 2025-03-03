
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
import ktorm.colonne
import ktorm.colonne.primaryKey
import ktorm.etage
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.Table
import org.ktorm.schema.int

import java.sql.DriverManager



@Composable
fun etageScreen(database : Database,ColonneSelectione : Int , onNavigate:(Routes)->Unit) {
    val etages = requestetage(ColonneSelectione)
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){

        Column {
            Button(
                onClick = {
                    onNavigate(Routes.COLONNE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }
            Text("Choisir un etage :")

            Button(
                onClick = {
                    onNavigate(Routes.CREATEETAGE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Créer un nouveau etage")
            }

            etages.forEach { etage ->
                Row {

                    Button(
                        onClick = {
                            onNavigate(Routes.EMPLACEMENT)

                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(etage.Numero_Etage.toString())
                    }

                    Button(
                        onClick = {
                            deleteEtage(database,etage)
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


fun requestetage(ColonneSelectione : Int ): List<etages> {
    val emplacements = mutableListOf<etages>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            val query = "SELECT * FROM `etage` WHERE ID_Etage = '"+ColonneSelectione+"'"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    emplacements.add(
                        etages(
                            ID_Etage = resultSet.getInt("etage.ID_Etage"),
                            Numero_Etage = resultSet.getInt("etage.Numero_Etage"),
                            ID_Colonne 	 = resultSet.getInt("etage.ID_Colonne"),
                            )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emplacements
}

data class etages(
    val ID_Etage: Int,
    val Numero_Etage: Int,
    val ID_Colonne 	: Int,


    )

fun deleteEtage(database : Database, Etage : etages): String {

    try {
        database.delete(ktorm.etage)
        {
            ktorm.etage.ID_Etage eq Etage.ID_Etage
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}

