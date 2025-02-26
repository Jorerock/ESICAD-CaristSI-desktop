package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.LoginScreen
import routing.Router
import routing.Routes
import ui.HomeScreen
import ui.LoginScreen
import ui.StockScreen
import ui.CaristesScreen


@Composable
@Preview
fun HomeScreen(onNavigate:(Routes)->Unit) {
    val router = remember { Router() }

    Column {
        Button(
            onClick = {
                onNavigate(Routes.HOME)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Suivis des colis")
        }
        Button(
            onClick = {
                onNavigate(Routes.STOCK)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion Stock")
        }
        Button(
            onClick = {
                onNavigate(Routes.CARISTES)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion Caristes")
        }
    }
    print("sortie homescreen")

}