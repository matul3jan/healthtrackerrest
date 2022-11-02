package ie.setu.api

import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.util.UserUtil
import ie.setu.utils.JsonUtil.jsonToObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiTest {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val util = UserUtil(origin)

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            setBasicAuthForTests()
        }

        @AfterAll
        @JvmStatic
        internal fun teardown() {
            clearBasicAuthForTests()
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `get all users from the database returns 200 response`() {
            val response = util.getUsers()
            assertEquals(200, response.status)
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {
            val retrieveResponse = util.retrieveUserById(Integer.MIN_VALUE)
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            val retrieveResponse = util.retrieveUserByEmail(nonExistingEmail)
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            // Add the user
            val addResponse = util.addUser()
            val addedUser: User = jsonToObject(addResponse.body.toString())

            // Retrieve the added user from the database and verify return code
            val retrieveResponse = util.retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            // Restore the db to previous state by deleting the added user
            util.deleteUser(addedUser.id)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            // Add the user
            util.addUser()

            // Retrieve the added user from the database and verify return code
            val retrieveResponse = util.retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            // Restore the db to previous state by deleting the added user
            val retrievedUser: User = jsonToObject(retrieveResponse.body.toString())
            util.deleteUser(retrievedUser.id)
        }
    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `add a user with correct details returns a 201 response`() {

            // Add the user and verify return code (using fixture data)
            val addResponse = util.addUser()
            assertEquals(201, addResponse.status)

            // Retrieve the added user from the database and verify return code
            val retrieveResponse = util.retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            // Verify the contents of the retrieved user
            val retrievedUser: User = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            // Restore the db to previous state by deleting the added user
            val deleteResponse = util.deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            // Add the user that we plan to do an update on
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"
            val addedResponse = util.addUser()
            val addedUser: User = jsonToObject(addedResponse.body.toString())

            // Update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, util.updateUser(addedUser.id).status)

            // Retrieve updated user and assert details are correct
            val updatedUserResponse = util.retrieveUserById(addedUser.id)
            val updatedUser: User = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedName, updatedUser.name)
            assertEquals(updatedEmail, updatedUser.email)

            // Restore the db to previous state by deleting the added user
            util.deleteUser(addedUser.id)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {
            assertEquals(404, util.updateUser(-1).status)
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            assertEquals(404, util.deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            // Add the user that we plan to do delete on
            val addedResponse = util.addUser()
            val addedUser: User = jsonToObject(addedResponse.body.toString())

            // Delete the added user and assert a 204 is returned
            assertEquals(204, util.deleteUser(addedUser.id).status)

            // Attempt to retrieve the deleted user --> 404 response
            assertEquals(404, util.retrieveUserById(addedUser.id).status)
        }
    }

    @Nested
    inner class UserStats {
        @Test
        fun `get user stats by id when user does not exist returns 404 response`() {
            val retrieveResponse = util.retrieveUserStatsById(Integer.MIN_VALUE)
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting user stats by id when id exists, returns a 200 response`() {

            // Add the user
            val addResponse = util.addUser()
            val addedUser: User = jsonToObject(addResponse.body.toString())

            // Retrieve the added user from the database and verify return code
            val retrieveResponse = util.retrieveUserStatsById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            // Restore the db to previous state by deleting the added user
            util.deleteUser(addedUser.id)
        }
    }
}