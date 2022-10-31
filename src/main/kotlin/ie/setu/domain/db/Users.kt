package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object Users: Table("Users") {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val age = integer("age")
    val gender = varchar("gender", 1)
    val height = integer("height")
    val weight = decimal("weight", 8, 2)
    val password = varchar("password", 255)
}