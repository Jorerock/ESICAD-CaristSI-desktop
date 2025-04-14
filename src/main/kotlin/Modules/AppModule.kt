package Modules

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.ktorm.database.Database


val databaseModule = module {
    single {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/carist-si",
            user = "root",
            password = "" // Chaîne vide au lieu de null si pas de mot de passe
        )
    }
}