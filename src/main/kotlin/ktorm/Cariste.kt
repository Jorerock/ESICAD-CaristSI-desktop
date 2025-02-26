package ktorm

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar


object Caristes : Table<Nothing>("caristes") {
    val id = int("ID").primaryKey()
    val nom = varchar("Nom")
    val prenom = varchar("Prenom")
    val mdp = varchar("MDP")
    val naissance = date("Naissance")
    val embauche = date("Embauche")
    val login = varchar("Login")
}