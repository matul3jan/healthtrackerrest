package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserDAO {

    fun findAll(): List<User> = transaction {
        Users.selectAll().map { mapToUser(it) }
    }

    fun findById(id: Int): User? = transaction {
        Users.select { Users.id eq id }.map { mapToUser(it) }.firstOrNull()
    }

    fun findByEmail(email: String): User? = transaction {
        Users.select { Users.email eq email }.map { mapToUser(it) }.firstOrNull()
    }

    fun save(user: User): Int = transaction {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[age] = user.age
            it[gender] = user.gender
            it[height] = user.height
            it[weight] = user.weight
        } get Users.id
    }

    fun delete(id: Int): Int = transaction {
        Users.deleteWhere {
            Users.id eq id
        }
    }

    fun update(id: Int, user: User): Int = transaction {
        Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[email] = user.email
            it[age] = user.age
            it[gender] = user.gender
            it[height] = user.height
            it[weight] = user.weight
        }
    }
}