package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object colis : Table<Nothing>("colis") {
    val ID = int("ID_Colis").primaryKey()
    val Longueur = int("Longueur")
    val Largeur = int("Largeur")
    val Hauteur = int("Hauteur")
    val Poids = int("Poids")
}

