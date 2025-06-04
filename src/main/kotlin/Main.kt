import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.*
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import ktorm.Caristes
import org.koin.core.context.startKoin
import androidx.compose.material.ButtonDefaults
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.*
import routing.Router
import routing.Routes
import ui.*
import ui.Element.FeedbackMessage
import ui.Element.OperationState
import Modules.databaseModule
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ktorm.colis
import ui.Element.*

fun main() = application {
    startKoin {
        modules(databaseModule)
    }

    var dbConnectionState by mutableStateOf<OperationState<Unit>>(OperationState.Loading)
    try {
        val database: Database = org.koin.core.context.GlobalContext.get().get()

        database.useConnection { connection ->
            val sql = "SELECT 1"
            connection.prepareStatement(sql).use { statement ->
                statement.executeQuery().asIterable().map {
                    println("Connexion à la base de données réussie: " + it.getString(1))
                    dbConnectionState = OperationState.Success(Unit)
                }
            }
        }
    } catch (e: Exception) {
        println("Échec de la connexion à la base de données: ${e.message}")
        dbConnectionState = OperationState.Error("Impossible de se connecter à la base de données: ${e.localizedMessage ?: "Erreur inconnue"}")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Carist-SI",
        state = WindowState(placement = WindowPlacement.Maximized)
    ) {
        // Si erreur bdd:
        when (val state = dbConnectionState) {
            is OperationState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Erreur de connexion",
                        style = MaterialTheme.typography.h4,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = ::exitApplication) {
                        Text("Quitter l'application")
                    }
                }
            }
            is OperationState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Connexion à la base de données...")
                }
            }
            is OperationState.Success -> {
                App()
            }
            else -> {
                // Ne devrait pas se produire, mais au cas où
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
@Preview
fun App() {
    val database: Database = org.koin.compose.koinInject()
    val router = remember { Router() }
    var currentSelectedID by remember { mutableStateOf(1) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            when (router.currentRoute) {
                Routes.LOGIN -> LoginScreen { email, password, updateState ->
                    println("Tentative de connexion avec $email et un mot de passe $password")

                    try {
                        val hasMatch = database.from(Caristes)
                            .select()
                            .where { (Caristes.login eq email) and (Caristes.mdp eq password) }
                            .iterator()
                            .hasNext()

                        if (hasMatch) {
                            updateState(OperationState.Success(Unit))
                            router.navigateTo(Routes.HOME)
                        } else {
                            updateState(OperationState.Error("Email ou mot de passe incorrect"))
                        }
                    } catch (e: Exception) {
                        updateState(OperationState.Error("Erreur de connexion: ${e.localizedMessage ?: "Erreur inconnue"}"))
                        println("Erreur de connexion: ${e.message}")
                    }
                }

                // Modifiez votre main.kt pour afficher InfoColis conditionnellement :

// Modifiez votre main.kt pour afficher InfoColis conditionnellement :

                Routes.RECHERCHE -> {
                    var searchState by remember { mutableStateOf<OperationState<Int>>(OperationState.Initial) }
                    var colisSelectionne by remember { mutableStateOf<Int?>(null) }

                    Column {
                        // Si un colis est sélectionné, affichez ses informations
                        if (colisSelectionne != null) {
                            InfoColis(database, colisSelectionne!!) { route ->
                                // En cas de retour, réinitialisez colisSelectionne à null
                                if (route == Routes.RECHERCHE) {
                                    colisSelectionne = null
                                } else {
                                    router.navigateTo(route)
                                }
                            }
                        } else {
                            // Sinon, affichez l'écran de recherche
                            RechercheColis(
                                { id ->
                                    try {
                                        println("recherche de ID_Colis $id")
                                        // Vérifier que l'ID existe dans la base de données
                                        val exists = database.from(colis)
                                            .select()
                                            .where { colis.ID eq id }
                                            .totalRecords > 0

                                        if (exists) {
                                            currentSelectedID = id
                                            colisSelectionne = id  // Définir le colis sélectionné au lieu de naviguer
                                            searchState = OperationState.Success(id)
                                        } else {
                                            searchState = OperationState.Error("Colis avec ID $id non trouvé")
                                        }
                                    } catch (e: Exception) {
                                        searchState = OperationState.Error("Erreur lors de la recherche: ${e.localizedMessage ?: "Erreur inconnue"}")
                                        println("Erreur lors de la recherche: ${e.message}")
                                    }
                                },
                                { route -> router.navigateTo(route) }
                            )

                            when (val state = searchState) {
                                is OperationState.Error -> {
                                    FeedbackMessage(message = state.message, isError = true)
                                }
                                is OperationState.Success -> {
                                    FeedbackMessage(message = "Colis trouvé avec succès", isError = false)
                                }
                                is OperationState.Loading -> {
                                    CircularProgressIndicator()
                                }
                                else -> { /* Ne rien afficher pour les autres états */ }
                            }
                        }
                    }
                }

//                Routes.ALLEE -> {
//                    var alleeState by remember { mutableStateOf<OperationState<Int>>(OperationState.Initial) }
//
//                    Column {
//                        AlleeScreen(
//                            database,
//                            { id ->
//                                try {
//                                    println("recherche de Colonne avec : $id")
//                                    val exists = database.from(colonne)
//                                        .select()
//                                        .where { colonne.ID_colonne eq id }
//                                        .totalRecordsInAllPages > 0
//
//                                    if (exists) {
//                                        currentSelectedID = id
//                                        alleeState = OperationState.Success(id)
//                                        router.navigateTo(Routes.COLONNE)
//                                    } else {
//                                        alleeState = OperationState.Error("Aucune colonne trouvée pour l'allée $id")
//                                    }
//                                } catch (e: Exception) {
//                                    alleeState = OperationState.Error("Erreur lors de la recherche: ${e.localizedMessage ?: "Erreur inconnue"}")
//                                    println("Erreur de recherche d'allée: ${e.message}")
//                                }
//                            },
//                            { route -> router.navigateTo(route) }
//                        )
//
//                        when (val state = alleeState) {
//                            is OperationState.Error -> {
//                                FeedbackMessage(message = state.message, isError = true)
//                            }
//                            is OperationState.Success -> {
//                                FeedbackMessage(message = "Colonnes trouvées avec succès", isError = false)
//                            }
//                            is OperationState.Loading -> {
//                                CircularProgressIndicator()
//                            }
//                            else -> { /* Ne rien afficher pour les autres états */ }
//                        }
//                    }
//                }

//                Routes.COLONNE -> colonneScreen(
//                    database,
//                    currentSelectedID,
//                    { id ->
//                        try {
//                            println("recherche de ETAGE avec : $id")
//                            currentSelectedID = id
//                            router.navigateTo(Routes.ETAGE)
//                        } catch (e: Exception) {
//                            println("Erreur lors de la recherche d'étage: ${e.message}")
//                            // Afficher un message d'erreur à l'utilisateur
//                        }
//                    },
//                    { route -> router.navigateTo(route) }
//                )

//                Routes.ETAGE -> etageScreen(
//                    database,
//                    currentSelectedID,
//                    { id ->
//                        try {
//                            println("recherche de Emplacement avec : $id")
//                            currentSelectedID = id
//                            router.navigateTo(Routes.EMPLACEMENT)
//                        } catch (e: Exception) {
//                            println("Erreur lors de la recherche d'emplacement: ${e.message}")
//                            // Afficher un message d'erreur à l'utilisateur
//                        }
//                    },
//                    { route -> router.navigateTo(route) }
//                )

//                Routes.EMPLACEMENT -> EmplacementScreen(
//                    database,
//                    currentSelectedID,
//                    { id ->
//                        try {
//                            println("recherche de Stock avec : $id")
//                            currentSelectedID = id
//                            router.navigateTo(Routes.STOCK)
//                        } catch (e: Exception) {
//                            println("Erreur lors de la recherche de stock: ${e.message}")
//                            // Afficher un message d'erreur à l'utilisateur
//                        }
//                    },
//                    { route -> router.navigateTo(route) }
//                )

                Routes.STOCK -> {
                    GestionStructures(onNavigate = { route -> router.navigateTo(route) })
                }

                Routes.HOME -> HomeScreen { route -> router.navigateTo(route) }

                Routes.CARISTES -> CaristesScreen(database) { route ->
                    router.navigateTo(route)
                }

                Routes.NEWCARISTES -> CreateCariste(database) { route ->
                    router.navigateTo(route)
                }

//                Routes.CREATEALLEE -> CreateAllee(database) { route ->
//                    router.navigateTo(route)
//                }

//                Routes.CREATECOLL -> CreateCollonne(database) { route ->
//                    router.navigateTo(route)
//                }

//                Routes.CREATEETAGE -> CreateEtage(database) { route ->
//                    router.navigateTo(route)
//                }

                Routes.CREATECOLIS -> CreateColis(database) { route ->
                    router.navigateTo(route)
                }

//                Routes.COLIS -> InfoColis(
//                    database,
//                    currentSelectedID
//                ) { route ->
//                    router.navigateTo(route)
//                }
//                else -> {
//                    // Cas par défaut si une route non gérée est rencontrée
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = "Route non trouvée: ${router.currentRoute}",
//                            style = MaterialTheme.typography.h5,
//                            color = Color.Red
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Button(
//                            onClick = { router.navigateTo(Routes.HOME) },
//                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor)
//                        ) {
//                            Text("Retourner à l'accueil", color = Color.White)
//                        }
//                    }
//                }
            }
        }
    }
}
