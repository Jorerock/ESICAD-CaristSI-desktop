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
import ktorm.colis.primaryKey
import ktorm.place
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.int

import java.sql.DriverManager
import java.time.LocalDate



@Composable
fun InfoColis(database : Database,ColisSelectione : Int , onNavigate:(Routes)->Unit) {
    var Stocks = requestColis(ColisSelectione)

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){

        Column {
            Button(
                onClick = {
                    onNavigate(Routes.RECHERCHE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            // Table Header
//            Row {
//                Text("ID", modifier = Modifier.weight(1f))
//                Text("Longueur", modifier = Modifier.weight(2f))
//                Text("Largeur", modifier = Modifier.weight(3f))
//                Text("Hauteur", modifier = Modifier.weight(3f))
//                Text("Poids", modifier = Modifier.weight(3f))
//                Text("Action", modifier = Modifier.weight(4f))
//
//            }
            // Table Rows
            Stocks.forEach { Stock ->
                var ID by remember { mutableStateOf(Stock.ID.toString()) }
                var Longueur by remember { mutableStateOf((Stock.Longueur.toString())) }
                var Largeur by remember { mutableStateOf(Stock.Largeur.toString())}
                var Hauteur by remember { mutableStateOf(Stock.Hauteur.toString())}
                var Poids by remember { mutableStateOf(Stock.Poids.toString())}

                var ID_Allee  by remember { mutableStateOf(Stock.ID_Allee.toString())}
                var Numero by remember { mutableStateOf(Stock.Numero.toString())}
                var ID_Colonne by remember { mutableStateOf(Stock.ID_Colonne.toString())}
                var NumeroCol by remember { mutableStateOf(Stock.NumeroCol.toString())}
                var ID_Etage by remember { mutableStateOf(Stock.ID_Etage.toString())}
                var Numero_Etage by remember { mutableStateOf(Stock.Numero_Etage.toString())}
                var ID_Emplacement by remember { mutableStateOf(Stock.ID_Emplacement.toString())}
                var VolumeMax by remember { mutableStateOf(Stock.VolumeMax.toString())}
                var PoidsMax by remember { mutableStateOf(Stock.PoidsMax.toString())}
                var result = ""







                Row {
                    Text(Stock.ID.toString(), modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        value = Longueur,
                        onValueChange = { newValue ->
                            // Vous pouvez ajouter une validation ici si nécessaire
                            Longueur = newValue
                            // Pour mettre à jour la valeur dans votre modèle:
                            // Convertir en Int avec gestion d'erreur
                            newValue.toIntOrNull()?.let { numCol ->
                                Longueur = numCol.toString()
                            }
                        },
                        label = { Text("Longueur") },
                        modifier = Modifier.weight(2f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = Largeur,
                        onValueChange = { newValue ->
                            // Vous pouvez ajouter une validation ici si nécessaire
                            Largeur = newValue
                            // Pour mettre à jour la valeur dans votre modèle:
                            // Convertir en Int avec gestion d'erreur
                            newValue.toIntOrNull()?.let { numCol ->
                                Largeur = numCol.toString()
                            }
                        },
                        label = { Text("Largeur") },
                        modifier = Modifier.weight(3f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = Hauteur,
                        onValueChange = { newValue ->
                            // Vous pouvez ajouter une validation ici si nécessaire
                            Hauteur = newValue
                            // Pour mettre à jour la valeur dans votre modèle:
                            // Convertir en Int avec gestion d'erreur
                            newValue.toIntOrNull()?.let { numCol ->
                                Hauteur = numCol.toString()
                            }
                        },
                        label = { Text("Largeur") },
                        modifier = Modifier.weight(4f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = Poids,
                        onValueChange = { newValue ->
                            // Vous pouvez ajouter une validation ici si nécessaire
                            Poids = newValue
                            // Pour mettre à jour la valeur dans votre modèle:
                            // Convertir en Int avec gestion d'erreur
                            newValue.toIntOrNull()?.let { numCol ->
                                Poids = numCol.toString()
                            }
                        },
                        label = { Text("Largeur") },
                        modifier = Modifier.weight(5f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

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



                Row {

                Button(
                        onClick = {
                            val updatedColis = Colliss(
                                ID = ID.toInt(),
                                Longueur = Longueur.toInt(),
                                Largeur = Largeur.toInt() ,
                                Hauteur = Hauteur.toInt(),
                                Poids = Poids.toInt(),
                                ID_Allee = ID_Allee.toInt(),
                                Numero = Numero.toInt(),
                                ID_Colonne = ID_Colonne.toInt(),
                                NumeroCol = NumeroCol.toInt(),
                                ID_Etage = ID_Etage.toInt(),
                                Numero_Etage = Numero_Etage.toInt(),
                                ID_Emplacement = ID_Emplacement.toInt(),
                                VolumeMax = VolumeMax.toInt(),
                                PoidsMax = PoidsMax.toInt(),
                            )
                            updateColis(database, updatedColis)
                        },

                        modifier = Modifier.weight(1f).padding(vertical = 8.dp)
                    ) {
                        Text("Modifier")
                    }


                    Button(
                        onClick = {
                            val updatedColis = Colliss(
                                ID = ID.toInt(),
                                Longueur = Longueur.toInt(),
                                Largeur = Largeur.toInt() ,
                                Hauteur = Hauteur.toInt(),
                                Poids = Poids.toInt(),
                                ID_Allee = ID_Allee.toInt(),
                                Numero = Numero.toInt(),
                                ID_Colonne = ID_Colonne.toInt(),
                                NumeroCol = NumeroCol.toInt(),
                                ID_Etage = ID_Etage.toInt(),
                                Numero_Etage = Numero_Etage.toInt(),
                                ID_Emplacement = ID_Emplacement.toInt(),
                                VolumeMax = VolumeMax.toInt(),
                                PoidsMax = PoidsMax.toInt(),
                            )

                            result = UpdateColisPosition(database,updatedColis,SelectEmplacement(updatedColis))
                        },
                        modifier = Modifier.weight(3f).padding(vertical = 8.dp)
                    ) {
                        Text("Modifier Position")
                    }

                    Button(
                        onClick = {
                            deleteColis(database,Stock)
                        },
                        modifier = Modifier.weight(3f).padding(vertical = 8.dp)
                    ) {
                        Text("Supprimer")
                    }
                    Text(result)

                }
            }
        }
    }
}


fun requestColis(ColisSelectione: Int ): List<Colliss> {
    val Stocks = mutableListOf<Colliss>()
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        println(ColisSelectione)
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
//            val query = "SELECT * FROM `colis` WHERE ID_Colis = '"+ColisSelectione.toString()+"'"
            val query = "select * from colis\n" +
                    "join place on colis.ID_Colis = place.ID_Colis\n" +
                    "join emplacement on place.ID_Emplacement = emplacement.ID_Emplacement\n" +
                    "join etage on etage.ID_Etage = emplacement.ID_Etage\n" +
                    "join colonne on etage.ID_Colonne = colonne.ID_Colonne\n" +
                    "join allee on colonne.ID_Allee = allee.ID_Allee\n" +
                    " WHERE colis.ID_Colis ='"+ColisSelectione.toString()+"'"
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    Stocks.add(
                        Colliss(
                            ID = resultSet.getInt("colis.ID_Colis"),
                            Longueur = resultSet.getInt("colis.Longueur"),
                            Largeur = resultSet.getInt("colis.Largeur") ,
                            Hauteur = resultSet.getInt("colis.Hauteur"),
                            Poids = resultSet.getInt("colis.Poids"),
                            Numero = resultSet.getInt("allee.Numero"),
                            ID_Allee  = resultSet.getInt("allee.ID_Allee"),
                            ID_Colonne  = resultSet.getInt("colonne.ID_Colonne"),
                            NumeroCol  = resultSet.getInt("colonne.NumeroCol"),
                            ID_Etage  = resultSet.getInt("etage.ID_Etage"),
                            Numero_Etage  = resultSet.getInt("etage.Numero_Etage"),
                            ID_Emplacement  = resultSet.getInt("emplacement.ID_Emplacement"),
                            VolumeMax  = resultSet.getInt("emplacement.VolumeMax"),
                            PoidsMax  = resultSet.getInt("emplacement.PoidsMax"),
                        )
                    )
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(Stocks)
    return Stocks
}



data class Colliss(
    val ID: Int,
    val Longueur: Int,
    val Largeur: Int,
    val Hauteur: Int,
    val Poids: Int,
//    allee
    val Numero: Int,
    val ID_Allee : Int,
//NumeroCol
    val  ID_Colonne:Int,
    val NumeroCol:Int,

    val ID_Etage:Int,
    val Numero_Etage:Int,

    val ID_Emplacement:Int,
    val VolumeMax:Int,
    val PoidsMax:Int,

)



fun deleteColis(database : Database, Colis : Colliss): String {

    try {
        database.delete(ktorm.colis)
        {
            ktorm.colis.ID eq Colis.ID
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}


fun SelectEmplacement(Colis: Colliss ): Int {
    val Stocks = Int
    val url = "jdbc:mysql://localhost:3306/carist-si"
    val user = "root"
    val password = ""


    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
//            val query = "SELECT * FROM `colis` WHERE ID_Colis = '"+ColisSelectione.toString()+"'"
            val query = "select emplacement.ID_Emplacement from emplacement\n" +
                    "join etage on etage.ID_Etage = emplacement.ID_Etage\n" +
                    "join colonne on etage.ID_Colonne = colonne.ID_Colonne\n" +
                    "join allee on colonne.ID_Allee = allee.ID_Allee\n" +
                    " WHERE allee.Numero = "+Colis.Numero+" and colonne.NumeroCol = "+Colis.NumeroCol+" and etage.Numero_Etage = "+Colis.Numero_Etage+""
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(query)
                while (resultSet.next()) {
                    return resultSet.getInt("emplacement.ID_Emplacement")

                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(Stocks)
    return Colis.ID_Emplacement
}

fun updateColis(database : Database, colis : Colliss): String {

    try {

        println("update colis"+ colis)
        database.update(ktorm.colis) {
            set(ktorm.colis.Largeur, colis.Largeur)
            set(ktorm.colis.Longueur, colis.Longueur)
            set(ktorm.colis.Hauteur, colis.Hauteur)
            set(ktorm.colis.Poids, colis.Poids)
            where {
                ktorm.colis.ID eq colis.ID
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}


fun UpdateColisPosition(database : Database, Collis : Colliss,Emplacement: Int): String {
    try {
        println("update cariste"+ colis)
        database.update(place) {
            set(place.ID_Emplacement, Emplacement)
            set(place.ID_cariste, 1)
            set(place.DateDepose, LocalDate.now())
            where {
                place.ID_Colis eq Collis.ID
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Vérifier si l'erreur concerne le dépassement de volume
        if (e.message?.contains("Le volume total des colis dépasserait le volume maximum") == true) {
            // Message d'erreur spécifique pour le dépassement de volume
            return "ERREUR_VOLUME"
        } else if (e.message?.contains("Le poids total des colis dépasserait le poids maximum") == true) {
            // Message d'erreur spécifique pour le dépassement de poids
            return "ERREUR_POIDS"
        }
    }
    return "done"
}

