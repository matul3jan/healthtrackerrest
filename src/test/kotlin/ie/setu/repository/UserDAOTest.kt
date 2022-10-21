package ie.setu.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.connectTempDatabase
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Retrieving some test data from Fixtures
private val user1 = users[0]
private val user2 = users[1]
private val user3 = users[2]

class UserDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            connectTempDatabase()
        }
    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                assertEquals(3, UserDAO.findAll().size)
                assertEquals(user1, UserDAO.findById(user1.id))
                assertEquals(user2, UserDAO.findById(user2.id))
                assertEquals(user3, UserDAO.findById(user3.id))
            }
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                assertEquals(3, UserDAO.findAll().size)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {
                populateUserTable()
                assertEquals(null, UserDAO.findById(4))
            }
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                populateUserTable()
                assertEquals(null, UserDAO.findById(4))
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {
                SchemaUtils.create(Users)
                assertEquals(0, UserDAO.findAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {
                populateUserTable()
                assertEquals(null, UserDAO.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {
                populateUserTable()
                assertEquals(user2, UserDAO.findByEmail(user2.email))
            }
        }
    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {
                populateUserTable()
                val user3Updated = User(3, "new username", "new@email.ie")
                UserDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated, UserDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existent user in table results in no updates`() {
            transaction {
                populateUserTable()
                val user4Updated = User(4, "new username", "new@email.ie")
                UserDAO.update(4, user4Updated)
                assertEquals(null, UserDAO.findById(4))
                assertEquals(3, UserDAO.findAll().size)
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existent user in table results in no deletion`() {
            transaction {
                populateUserTable()
                assertEquals(3, UserDAO.findAll().size)
                UserDAO.delete(4)
                assertEquals(3, UserDAO.findAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {
                populateUserTable()
                assertEquals(3, UserDAO.findAll().size)
                UserDAO.delete(user3.id)
                assertEquals(2, UserDAO.findAll().size)
            }
        }
    }
}