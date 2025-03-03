package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object place : Table<Nothing>("place") {
    val ID_cariste = int("ID_cariste").primaryKey()
    val ID_Colis = int("ID_Colis").primaryKey()
    val ID_Emplacement = int("ID_Emplacement").primaryKey()
    val DateDepose = date("DateDepose")
}
