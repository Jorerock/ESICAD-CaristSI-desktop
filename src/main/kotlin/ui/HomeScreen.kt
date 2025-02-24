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


@Composable
@Preview
fun HomeScreen() {
    val router = remember { Router() }

    Column {
        Text("Coucou")

        Button(
            onClick = {
                print("Go to HOME")
                router.navigateTo(route = Routes.HOME)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Suivis des colis")
        }
        Button(
            onClick = {
                print("Go to LOGIN")

                router.navigateTo(route = Routes.LOGIN)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestion structure")
        }
    }
}