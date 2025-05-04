//package ui
//import ktorm.colonne
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import routing.Routes
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.sp
//import ktorm.allee
//import org.ktorm.database.Database
//import org.ktorm.dsl.*
//
//
//@Composable
//fun CreateCollonne(database : Database, onNavigate:(Routes)->Unit) {
//    var NumeroCol by remember { mutableStateOf(TextFieldValue(colonne.NumeroCol.toString())) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//
//
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(16.dp), elevation = 4.dp
//    ){
//        var Alee  = 1
//
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Button(
//                onClick = {
//                    onNavigate(Routes.COLONNE)
//                },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text("Retour")
//            }
//
//            Text("Creation Cariste", fontSize = 24.sp)
//
//            OutlinedTextField(
//                value = NumeroCol,
//                onValueChange = { NumeroCol = it },
//                label = { Text("NumeroCol") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                modifier = Modifier.fillMaxWidth()
//            )
//            errorMessage?.let {
//                Text(it, color = MaterialTheme.colors.error)
//            }
//
//
//            Button(
//                onClick = {
//                    val updatedColl = NewColl(
//                        NumeroCol = NumeroCol.text.toInt(),
//                        ID_colonne = 0
//                    )
//                    insertColl(database, updatedColl)
//                },
//
//                modifier = Modifier.weight(4f).padding(vertical = 8.dp)
//            ) {
//                Text("Cree Nouvelle collonne`")
//
//            }
//        }
//    }
//}
//
//
//data class NewColl(
//    val ID_colonne: Int,
//    val NumeroCol: Int,
//
//    )
//
//
//fun insertColl(database : Database, NewColl : NewColl): String {
//
//    try {
//
//        println("update allee"+ allee)
//        database.insert(ktorm.colonne) {
//            set(colonne.NumeroCol, NewColl.NumeroCol)
//        }
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return "done"
//}