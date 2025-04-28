package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import routing.Routes
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import ktorm.colis

@Composable
fun RechercheColis(onRecherche:(Int)->Unit , onNavigate:(Routes)->Unit) {
    var Selection : Int
//    var Stocks = request(Selection)

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

            // Table Header
//            Row {
//                Text("ID", modifier = Modifier.weight(1f))
//                Text("Longueur", modifier = Modifier.weight(2f))
//                Text("Largeur", modifier = Modifier.weight(3f))
//                Text("Poids", modifier = Modifier.weight(4f))
//            }
                var ID by remember { mutableStateOf("") }
                var Longueur by remember { mutableStateOf((colis.Longueur.toString())) }
                var Largeur by remember { mutableStateOf(colis.Largeur.toString()) }
                var Poids by remember { mutableStateOf(colis.Poids.toString()) }

                Row {
                    OutlinedTextField(
                        value = ID,
                        onValueChange = { newValue ->
                            // Vous pouvez ajouter une validation ici si nécessaire
                            ID = newValue
                            // Pour mettre à jour la valeur dans votre modèle:
                            // Convertir en Int avec gestion d'erreur
                            newValue.toIntOrNull()?.let { numCol ->
                                ID = numCol.toString()
                            }
                        },
                        label = { Text("ID") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
//
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
                }
            Row {

                    Button(
                        onClick = {
//                            val updatedColiss = Colliss(
//                                ID = ID.toInt(),
//                                Longueur = Longueur.toInt(),
//                                Largeur = Largeur.toInt() ,
//                                Hauteur = Hauteur.toInt(),
//                                Poids = Poids.toInt(),
//                            )
                            onRecherche(ID.toInt())
                        },

                        modifier = Modifier.weight(4f).padding(vertical = 8.dp)
                    ) {
                        Text("Recherche")
                    }

            }
            Row {
                Button(
                onClick = {
                    onNavigate(Routes.CREATECOLIS)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Nouveau Colis")
            } }
            }
        }
    }


