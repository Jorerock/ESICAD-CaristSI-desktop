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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import ktorm.allee
import org.ktorm.database.Database
import org.ktorm.dsl.*


@Composable
fun CreateAllee(database : Database, onNavigate:(Routes)->Unit) {
    var Numero by remember { mutableStateOf(TextFieldValue(allee.Numero.toString())) }
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
                    onNavigate(Routes.ALLEE)
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Retour")
            }

            Text("Creation Cariste", fontSize = 24.sp)

            OutlinedTextField(
                value = Numero,
                onValueChange = { Numero = it },
                label = { Text("Numero") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            errorMessage?.let {
                Text(it, color = MaterialTheme.colors.error)
            }


            Button(
                onClick = {
                    val updatedallee = NewAllee(
                        Numero = Numero.text.toInt(),
                        ID_Allee = 0
                    )
                    insertAllee(database, updatedallee)
                },

                modifier = Modifier.weight(4f).padding(vertical = 8.dp)
            ) {
                Text("Cree nouvelle alleee")

            }
        }
    }
}


data class NewAllee(
    val ID_Allee: Int,
    val Numero: Int,

    )


fun insertAllee(database : Database, NewAllee : NewAllee): String {

    try {

        println("update allee"+ allee)
        database.insert(allee) {
            set(allee.Numero, NewAllee.Numero)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "done"
}