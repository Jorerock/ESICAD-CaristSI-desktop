//package ui
//
//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import ui.LoginScreen
//import routing.Router
//import routing.Routes
//import ui.HomeScreen
//import ui.LoginScreen
//import ui.StockScreen
//import ui.CaristesScreen
//import ui.Element.*
//
//
//@Composable
//@Preview
//fun HomeScreen(onNavigate:(Routes)->Unit) {
//    val router = remember { Router() }
//
//
//    Column {
//        Button(
//            onClick = {
//                onNavigate(Routes.RECHERCHE)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(6.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
//        ) {
//            Text("Suivis des colis")
//        }
//
//        Button(
//            onClick = {
//                onNavigate(Routes.CARISTES)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(6.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
//        ) {
//            Text("Gestion des caristes")
//        }
//
//        Button(
//            onClick = {
//                onNavigate(Routes.ALLEE)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(6.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
//        ) {
//            Text("Gestion du stockage")
//        }
//    }
//}


package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import routing.Router
import routing.Routes
import ui.Element.*

@Composable
@Preview
fun HomeScreen(onNavigate: (Routes) -> Unit) {
    val router = remember { Router() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Titre
        Text(
            text = "Carist-SI",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Système de gestion d'entrepôt",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Menu principal
        MenuButton(
            title = "Suivi des colis",
            description = "Rechercher et gérer les informations des colis",
            onClick = { onNavigate(Routes.RECHERCHE) }
        )

        MenuButton(
            title = "Gestion des caristes",
            description = "Ajouter, modifier ou supprimer des utilisateurs",
            onClick = { onNavigate(Routes.CARISTES) }
        )

        MenuButton(
            title = "Gestion du stockage",
            description = "Gérer les allées, colonnes et emplacements",
            onClick = { onNavigate(Routes.ALLEE) }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Version ou infos supplémentaires
        Text(
            text = "Carist-SI v1.0",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MenuButton(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = primaryColor.copy(alpha = 0.9f),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}