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
fun StockScreen(database : Database,AlleeSelectione : Int ,onRechercheColis:(Int)->Unit, onNavigate:(Routes)->Unit) {
    var Selection = AlleeSelectione
    var Stocks = request(Selection)

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
            Text("Voicie les colis de l'emplacement selectionne : ")

            // Table Header
            Row {
                Text("Id_Colis", modifier = Modifier.padding(vertical = 8.dp).weight(1f))
                Text("Longueur", modifier = Modifier.padding(vertical = 8.dp).weight(2f))
                Text("Largeur", modifier = Modifier.padding(vertical = 8.dp).weight(3f))
                Text("Hauteur", modifier = Modifier.padding(vertical = 8.dp).weight(4f))
                Text("Poids", modifier = Modifier.padding(vertical = 8.dp).weight(5f))

            }
            // Table Rows
            Stocks.forEach { Stock ->
                var ID_Colis by remember { mutableStateOf(Stock.ID_Colis) }
                var Longueur by remember { mutableStateOf((Stock.Longueur.toString())) }
                var Largeur by remember { mutableStateOf(Stock.Largeur.toString())}
                var Hauteur by remember { mutableStateOf(Stock.Hauteur.toString())}
                var Poids by remember { mutableStateOf(Stock.Poids.toString())}

                Row {
                    Button(
                        onClick = {
                            onRechercheColis(ID_Colis)
//                            onNavigate(Routes.EMPLACEMENT)
                        },
                        modifier = Modifier.padding(vertical = 8.dp).weight(1f)
                    ) {
                        Text(ID_Colis.toString())
                    }

                    Text(Longueur, modifier = Modifier.padding(vertical = 8.dp).weight(2f))
                    Text(Largeur, modifier = Modifier.padding(vertical = 8.dp).weight(3f))
                    Text(Hauteur, modifier = Modifier.padding(vertical = 8.dp).weight(4f))
                    Text(Poids, modifier = Modifier.padding(vertical = 8.dp).weight(5f))


//                    OutlinedTextField(
//                        value = Longueur,
//                        onValueChange = { newValue ->
//                            // Vous pouvez ajouter une validation ici si nécessaire
//                            Longueur = newValue
//                            // Pour mettre à jour la valeur dans votre modèle:
//                            // Convertir en Int avec gestion d'erreur
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Longueur = numCol.toString()
//                            }
//                        },
//                        label = { Text("Longueur") },
//                        modifier = Modifier.weight(2f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//
//                    OutlinedTextField(
//                        value = Largeur,
//                        onValueChange = { newValue ->
//                            // Vous pouvez ajouter une validation ici si nécessaire
//                            Largeur = newValue
//                            // Pour mettre à jour la valeur dans votre modèle:
//                            // Convertir en Int avec gestion d'erreur
//                            newValue.toIntOrNull()?.let { numCol ->
//                                Largeur = numCol.toString()
//                            }
//                        },
//                        label = { Text("Largeur") },
//                        modifier = Modifier.weight(3f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )

//                    Button(
//                        onClick = {
//                            val updatedStocks = Stock(
//                                ID_Colis = NumeroCol.toInt(),
//                                Longueur = Longueur.toInt(),
//                                Largeur = Largeur.toInt()
//                            )
//                            sendData(database, updatedStocks)
//                                  },
//
//                        modifier = Modifier.weight(4f).padding(vertical = 8.dp)
//                    ) {
//                        Text("Modifier")
//                    }
                }
            }
        }
    }
}


fun request(AlleeSelectione: Int ): List<Stock> {
    val Stocks = mutableListOf<Stock>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            val query = "select * from colis \n" +
                    "join place on colis.ID_Colis = place.ID_Colis \n" +
                    "join emplacement on place.ID_Emplacement = emplacement.ID_Emplacement \n" +
                    "join etage on etage.ID_Etage = emplacement.ID_Etage\n" +
                    "join colonne on etage.ID_Colonne = colonne.ID_Colonne \n" +
                    "join allee on colonne.ID_Allee = allee.ID_Allee\n" +
                    "where emplacement.ID_Emplacement = '"+AlleeSelectione.toString()+"'"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    Stocks.add(
                        Stock(
                            ID_Colis = resultSet.getInt("colis.ID_Colis"),
                            Longueur = resultSet.getInt("colis.Longueur"),
                            Largeur = resultSet.getInt("colis.Largeur"),
                            Hauteur = resultSet.getInt("colis.Hauteur"),
                            Poids = resultSet.getInt("colis.Poids")
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("Stocks trouve : "+Stocks)
    return Stocks
}

data class Stock(
    val ID_Colis: Int,
    val Longueur: Int,
    val Largeur: Int,
    val Hauteur: Int,
    val Poids: Int,

)

//fun sendData(database : Database, stock : Stock): String {
//
//    try {
//
//        println("update cariste"+ stock)
//        database.update(Caristes) {
//            set(colis.Largeur, stock.Largeur)
//            set(colis.Longueur, stock.Longueur)
//            where {
//                Caristes.ID_cariste eq stock.ID_Colis
//            }
//        }
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return "done"
//}

