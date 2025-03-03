package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object allee : Table<Nothing>("allee") {
    val ID_Allee = int("ID_Allee").primaryKey()
    val Numero = int("Numero")
}

