import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
//import androidx.compose.ui.window
import androidx.compose.ui.window.*
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import ktorm.Caristes
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import routing.Router
import routing.Routes
import ui.*


@Composable
@Preview
fun App(database: Database) {
    val router = remember { Router() }
    val ID_cariste : Int = remember {0 }
    var currentSelectedID = 1
    MaterialTheme {
        Surface {
            when (router.currentRoute) {

                Routes.LOGIN -> LoginScreen({ email, password ->
                    println("Tentative de connexion avec $email et un mot de passe ${password}")
                    if ((database.from(Caristes).select().where {
                            (Caristes.login eq email) and (Caristes.mdp eq password)
                        }).iterator().hasNext()) {

                        router.navigateTo(route = Routes.HOME)
                    }
                })



                Routes.RECHERCHE -> RechercheColis({ ID1 ->
                    println("recherche de ID_Colis "+ID1)
                        router.navigateTo(route = Routes.COLIS)
                        currentSelectedID = ID1
                },{route -> router.navigateTo(route)})

                Routes.ALLEE -> AlleeScreen(database,{ ID_Allee ->
                    println("recherche de Collone avec : "+ID_Allee)
                        router.navigateTo(route = Routes.COLONNE)
                        currentSelectedID = ID_Allee
                },{route -> router.navigateTo(route)})

                Routes.COLONNE -> colonneScreen(database,currentSelectedID,{ ID_colonne ->
                    println("recherche de ETAGE avec : "+ID_colonne)
                        router.navigateTo(route = Routes.ETAGE)
                        currentSelectedID = ID_colonne
                },{route -> router.navigateTo(route)})

                Routes.ETAGE -> etageScreen(database,currentSelectedID,{ ID_Etage ->
                    println("recherche de Emplacement avec : "+ID_Etage)
                        router.navigateTo(route = Routes.EMPLACEMENT)
                        currentSelectedID = ID_Etage
                },{route -> router.navigateTo(route)})

                Routes.EMPLACEMENT -> EmplacementScreen(database,currentSelectedID,{ ID_Emplacement ->
                    println("recherche de Emplacement avec : "+ID_Emplacement)
                        router.navigateTo(route = Routes.STOCK)
                        currentSelectedID = ID_Emplacement
                },{route -> router.navigateTo(route)})

                Routes.STOCK -> StockScreen(database,currentSelectedID,{ ID_Colis ->
                    println("recherche de Emplacement avec : "+ID_Colis)
                        router.navigateTo(route = Routes.COLIS)
                        currentSelectedID = ID_Colis
                },{route -> router.navigateTo(route)})



//                Routes.STOCK -> StockScreen(database,currentSelectedID){ route -> router.navigateTo(route)}

//                Routes.ETAGE -> etageScreen(database,currentSelectedID){ route -> router.navigateTo(route)}

//                Routes.COLONNE -> colonneScreen(database,currentSelectedID){ route -> router.navigateTo(route)}

                Routes.HOME -> HomeScreen{route -> router.navigateTo(route)}
                Routes.CARISTES -> CaristesScreen(database){ route -> router.navigateTo(route)}
                Routes.NEWCARISTES -> CreateCariste(database){ route -> router.navigateTo(route)}
                Routes.CREATEALLEE -> CreateAllee(database){ route -> router.navigateTo(route)}
                Routes.CREATECOLL -> CreateCollonne(database){ route -> router.navigateTo(route)}
                Routes.CREATEETAGE -> CreateEtage(database){ route -> router.navigateTo(route)}
                Routes.CREATECOLIS -> CreateColis(database){ route -> router.navigateTo(route)}
                Routes.COLIS -> InfoColis(database,currentSelectedID){ route -> router.navigateTo(route)}


            }



        }
    }
}

fun main() = application {

        val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/carist-si",
        user = "root",
        password = null
    )

    database.useConnection { connection ->
        val sql = "SELECT 1"
        connection.prepareStatement(sql).use { statement ->
            statement.executeQuery().asIterable().map {
                println("it worked : " + it.getString(1))
            }
        }
    }

    Window(onCloseRequest = ::exitApplication,
        title = "Carist-SI",
        state = WindowState(placement = WindowPlacement.Maximized)
        ) {
        App(database)


    }
}
