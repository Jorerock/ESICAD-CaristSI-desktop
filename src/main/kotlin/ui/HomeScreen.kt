package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.LoginScreen

@Composable
@Preview
fun HomeScreen() {
    Column {
        Text("Coucou")

        Button(
            onClick = {
           true},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Suivis des colis")
        }
        Button(
            onClick = {
                true},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion structure")
        }
    }
}