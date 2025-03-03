package ui
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SimpleDatePicker(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var showDialog by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column {
        OutlinedTextField(
            value = selectedDate.format(formatter),
            onValueChange = { /* Readonly */ },
            label = { Text("Date") },
            modifier = Modifier.clickable { showDialog = true },
            readOnly = true
        )

        // Ici vous pourriez implémenter un dialogue personnalisé pour sélectionner la date
        // quand showDialog est true
    }

    // Quand une date est sélectionnée
    LaunchedEffect(selectedDate) {
        onDateSelected(selectedDate)
    }
}