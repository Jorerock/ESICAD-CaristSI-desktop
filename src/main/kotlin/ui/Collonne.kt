
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

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.int

import java.sql.DriverManager



@Composable
fun colonneScreen(database : Database, AlleeSelectione : Int ,onRechercheEtage:(Int)->Unit ,onNavigate:(Routes)->Unit) {
    val Colonnes = requestColonne(AlleeSelectione)
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){

        Column {
            Button(
                onClick = {
                    onNavigate(Routes.ALLEE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            Button(
                onClick = {
                    onNavigate(Routes.CREATECOLL)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Créer une nouvelle Collonne")
            }


            Text("Choisir une Collonne :")

            Colonnes.forEach { Colonne ->
                Row {
                    Button(
                        onClick = {
                            onRechercheEtage(Colonne.ID_colonne)

//                            onNavigate(Routes.ETAGE)
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(Colonne.NumeroCol.toString())
                    }

                    Button(
                        onClick = {
                            deleteCollonne(database,Colonne)
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


fun requestColonne(AlleeSelectione : Int ): List<Collonne> {
    val colonnes = mutableListOf<Collonne>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            val query = "SELECT * FROM `colonne` WHERE ID_Allee = '"+AlleeSelectione+"'"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    colonnes.add(
                        Collonne(
                            ID_colonne = resultSet.getInt("colonne.ID_colonne"),
                            NumeroCol = resultSet.getInt("colonne.NumeroCol"),
                            ID_Allee = resultSet.getInt("colonne.ID_Allee"),

                            )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return colonnes
}




data class Collonne(
    val ID_colonne: Int,
    val NumeroCol: Int,
    val ID_Allee: Int,

    )


fun deleteCollonne(database : Database, Collonne : Collonne): String {

    try {
        database.delete(ktorm.colonne)
        {
            ktorm.colonne.ID_colonne eq Collonne.ID_colonne
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}

