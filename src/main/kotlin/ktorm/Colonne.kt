package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object colonne : Table<Nothing>("colonne") {
    val ID_colonne = int("ID_colonne").primaryKey()
    val NumeroCol = int("NumeroCol")
    val ID_Allee = int("ID_Allee")
}

