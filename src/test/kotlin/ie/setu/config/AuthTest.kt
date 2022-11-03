package ie.setu.config

import ie.setu.helpers.ServerContainer
import ie.setu.util.AuthUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthTest {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val util = AuthUtil(origin)

    @Test
    fun `user without any role should access documentation`() {
        val responseUI = util.openSwaggerUi()
        val responseDocs = util.openSwaggerDocs()
        val responseRedoc = util.openRedoc()
        assertEquals(200, responseUI.status)
        assertEquals(200, responseDocs.status)
        assertEquals(200, responseRedoc.status)
    }

    @Test
    fun `user without any role should not access anything`() {
        val responseUsers = util.getAllUsers()
        val responseActivities = util.getAllActivities()
        val responseGoals = util.getAllGoals()
        assertEquals(401, responseUsers.status)
        assertEquals(401, responseActivities.status)
        assertEquals(401, responseGoals.status)
    }

    @Test
    fun `user with admin role should access anything`() {
        val responseUsers = util.getAllUsersAsAdmin()
        val responseActivities = util.getAllActivitiesAsAdmin()
        val responseGoals = util.getAllGoalsAsAdmin()
        assertEquals(200, responseUsers.status)
        assertEquals(200, responseActivities.status)
        assertEquals(200, responseGoals.status)
    }

    @Test
    fun `user with tester role should access anything`() {
        val responseUsers = util.getAllUsersAsTester()
        val responseActivities = util.getAllActivitiesAsTester()
        val responseGoals = util.getAllGoalsAsTester()
        assertEquals(200, responseUsers.status)
        assertEquals(200, responseActivities.status)
        assertEquals(200, responseGoals.status)
    }
}