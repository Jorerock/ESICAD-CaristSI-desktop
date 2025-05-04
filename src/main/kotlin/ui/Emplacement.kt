//package ui
//
//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import ui.LoginScreen
//import routing.Router
//import routing.Routes
//import ui.HomeScreen
//import ui.LoginScreen
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import ktorm.Caristes
//import ktorm.colonne
//import ktorm.colonne.primaryKey
//import org.ktorm.database.Database
//import org.ktorm.dsl.*
//import org.ktorm.schema.int
//import ktorm.emplacements
//
//
//import java.sql.DriverManager
//
//
//
//@Composable
//fun EmplacementScreen(database : Database,ColonneSelectione : Int ,onRechercheStock:(Int)->Unit, onNavigate:(Routes)->Unit) {
//    val emplacements = requestemplacement(ColonneSelectione)
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
//    ){
//
//        Column {
//            Button(
//                onClick = {
//                    onNavigate(Routes.ETAGE)
//                },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text("Retour")
//            }
//            Text("Emplacement de la Colonne: "+ ColonneSelectione)
//
//            Row {
//                Text("VolumeMax", modifier = Modifier.weight(2f))
//                Text("PoidsMax", modifier = Modifier.weight(3f))
//                Text("Action", modifier = Modifier.weight(4f))
//
//            }
//
//
//            emplacements.forEach { emplacement ->
//                var VolumeMaxChange by remember { mutableStateOf(TextFieldValue(emplacement.VolumeMax.toString())) }
//                var PoidsMaxChange by remember { mutableStateOf(TextFieldValue(emplacement.PoidsMax.toString())) }
//                Row {
////                    Text(emplacement.ID_emplacement .toString(), modifier = Modifier.weight(1f))
//
//                    OutlinedTextField(
//                        value = VolumeMaxChange,
//                        onValueChange = { VolumeMaxChange = it },
//                        label = { VolumeMaxChange },
//                        modifier = Modifier.weight(2f)
//                    )
//                    OutlinedTextField(
//                        value = PoidsMaxChange,
//                        onValueChange = { PoidsMaxChange = it },
//                        label = { PoidsMaxChange },
//                        modifier = Modifier.weight(3f)
//                    )
//
//                    Button(
//                        onClick = {
//                            val updatedemplacements = emplacement(
//                                ID_Emplacement = emplacement.ID_Emplacement,
//                                VolumeMax = VolumeMaxChange.text.toInt(),
//                                PoidsMax = PoidsMaxChange.text.toInt(),
//                                ID_Etage = emplacement.ID_Etage
//                            )
//                            UpdateEmplacement(database, updatedemplacements)
//                        },
//
//                        modifier = Modifier.weight(4f).padding(vertical = 8.dp)
//                    ) {
//                        Text("Modifier")
//
//                    }
//                }
//                Row {
//                    Button(
//                        onClick = {
//                            onRechercheStock(emplacement.ID_Emplacement)
////                            onNavigate(Routes.STOCK)
//                        },
//                        modifier = Modifier.weight(5f).padding(vertical = 8.dp)
//                    ) {
//                        Text("Voir colis")
//                    }
//                }
//            }
//
//        }
//    }
//}
//
//
//fun requestemplacement(ColonneSelectione : Int ): List<emplacement> {
//    val emplacements = mutableListOf<emplacement>()
//    val url = "jdbc:mysql://localhost:3306/carist-si"
//    val user = "root"
//    val password = ""
//
//
//    try {
//        Class.forName("com.mysql.cj.jdbc.Driver")
//        DriverManager.getConnection(url, user, password).use { connection ->
//            val query = "SELECT * FROM `emplacement` WHERE  ID_Etage = '"+ColonneSelectione+"'"
//            connection.createStatement().use { statement ->
//                val resultSet = statement.executeQuery(query)
//                while (resultSet.next()) {
//                    emplacements.add(
//                        emplacement(
//                            ID_Emplacement = resultSet.getInt("emplacement.ID_Emplacement"),
//                            VolumeMax = resultSet.getInt("emplacement.VolumeMax"),
//                            PoidsMax = resultSet.getInt("emplacement.PoidsMax"),
//                            ID_Etage = resultSet.getInt("emplacement.ID_Etage"),
//
//                            )
//                    )
//                }
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return emplacements
//}
//
//fun UpdateEmplacement(database : Database, emplacement : emplacement): String {
//
//    try {
//
//        println("update emplacement"+ emplacement)
//        database.update(emplacements) {
//            set(emplacements.VolumeMax, emplacement.VolumeMax)
//            set(emplacements.PoidsMax, emplacement.PoidsMax)
//            where {
//                emplacements.ID_Emplacement eq emplacement.ID_Emplacement
//            }
//        }
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return "done"
//}
//
//
//data class emplacement(
//    val ID_Emplacement: Int,
//    val VolumeMax: Int,
//    val PoidsMax: Int,
//    val ID_Etage: Int,
//    )
//
