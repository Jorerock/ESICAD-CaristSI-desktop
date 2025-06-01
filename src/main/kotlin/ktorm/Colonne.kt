package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object colonne_table : org.ktorm.schema.Table<Nothing>("colonne") {
    val ID_Colonne = int("ID_Colonne").primaryKey()
    val ID_Allee = int("ID_Allee")
    val NumeroCol = int("NumeroCol")
}

