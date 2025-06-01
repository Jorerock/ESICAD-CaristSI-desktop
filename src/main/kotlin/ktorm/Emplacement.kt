package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object emplacements : Table<Nothing>("emplacement") {
    val ID_Emplacement = int("ID_Emplacement").primaryKey()
    val VolumeMax = int("VolumeMax")
    val PoidsMax = int("PoidsMax")
    val ID_Etage = int(" ID_Etage").primaryKey()

}


