package ui

import ktorm.etage
import ktorm.colonne
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import ktorm.allee
import org.ktorm.database.Database
import org.ktorm.dsl.*


@Composable
fun CreateEtage(database : Database, onNavigate:(Routes)->Unit) {
    var Numero_Etage by remember { mutableStateOf(TextFieldValue(etage.Numero_Etage.toString())) }
    var errorMessage by remember { mutableStateOf<String?>(null) }



    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
    ){
        var Alee  = 1

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    onNavigate(Routes.COLONNE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            Text("Creation Cariste", fontSize = 24.sp)

            OutlinedTextField(
                value = Numero_Etage,
                onValueChange = { Numero_Etage = it },
                label = { Text("Numero_Etage") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }


            Button(
                onClick = {
                    val updatedEtage = NewEtage(
                        Numero_Etage = Numero_Etage.text.toInt()
                    )
                    insertEtage(database, updatedEtage)
                },

                modifier = Modifier.weight(4f).padding(vertical = 8.dp)
            ) {
                Text("Cree Nouvelle collonne`")

            }
        }
    }
}


data class NewEtage(
    val Numero_Etage: Int,

    )


fun insertEtage(database : Database, NewEtage : NewEtage): String {

    try {

        println("update allee"+ allee)
        database.insert(ktorm.etage) {
            set(etage.Numero_Etage, NewEtage.Numero_Etage)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}