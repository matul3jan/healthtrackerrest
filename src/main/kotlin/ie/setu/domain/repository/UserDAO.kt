package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    fun findAll(): List<User> = transaction {
        Users.selectAll().map { mapToUser(it) }
    }

    fun finById(id: Int): User? = transaction {
        Users.select { Users.id eq id }.map { mapToUser(it) }.firstOrNull()
    }

    fun findByEmail(email: String): User? = transaction {
        Users.select { Users.email eq email }.map { mapToUser(it) }.firstOrNull()
    }

    fun save(user: User) = transaction {
        Users.insert {
            it[name] = user.name
            it[email] = user.email
        }
    }

    fun delete(id: Int): Int = transaction {
        Users.deleteWhere {
            Users.id eq id
        }
    }

    fun update(id: Int, user: User) = transaction {
        Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[email] = user.email
        }
    }
}