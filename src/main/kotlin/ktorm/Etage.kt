package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object etage : Table<Nothing>("colonne") {
    val ID_Etage = int("ID_Etage").primaryKey()
    val Numero_Etage = int("Numero_Etage")
    val ID_Colonne = int("ID_Colonne")
}

