package ie.setu.api

import ie.setu.config.DBConfig
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.util.ActivityUtil
import ie.setu.util.UserUtil
import ie.setu.utils.JsonUtil.jsonNodeToObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityApiTest {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val activityUtil = ActivityUtil(origin)
    private val userUtil = UserUtil(origin)

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            DBConfig.createDbConnection()
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get all activities from the database returns 200 response`() {
            val response = activityUtil.getActivities()
            assertEquals(200, response.status)
        }

        @Test
        fun `get activity by id when activity does not exist returns 404 response`() {
            val retrieveResponse = activityUtil.getActivityById(-1)
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {

            // Add a user
            val addedUser: User = jsonNodeToObject(userUtil.addUser())

            // Add associated activity
            val activity = activities[0].copy(userId = addedUser.id)
            val addActivityResponse = activityUtil.addActivity(activity)

            assertEquals(201, addActivityResponse.status)

            val addedActivity: Activity = jsonNodeToObject(addActivityResponse)

            // Retrieve the activity by activity id
            val response = activityUtil.getActivityById(addedActivity.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, userUtil.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            val response = activityUtil.retrieveByUserId(-1)
            assertEquals(404, response.status)
        }

        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())

            activityUtil.addActivity(activities[0].copy(userId = addedUser.id))
            activityUtil.addActivity(activities[1].copy(userId = addedUser.id))
            activityUtil.addActivity(activities[2].copy(userId = addedUser.id))

            val response = activityUtil.retrieveByUserId(addedUser.id)
            assertEquals(200, response.status)

            val retrievedActivities: Array<Activity> = jsonNodeToObject(response)
            assertEquals(3, retrievedActivities.size)

            assertEquals(204, userUtil.deleteUser(addedUser.id).status)
        }
    }

    @Nested
    inner class CreateActivities {
        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {
            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addActivityResponse = activityUtil.addActivity(activity)
            assertEquals(201, addActivityResponse.status)
            userUtil.deleteUser(addedUser.id)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            val userId = -1
            assertEquals(404, activityUtil.retrieveByUserId(userId).status)

            val activity = activities[0].copy(userId = userId)
            val addActivityResponse = activityUtil.addActivity(activity)

            assertEquals(404, addActivityResponse.status)
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1
            assertEquals(404, activityUtil.retrieveByUserId(userId).status)
            assertEquals(404, activityUtil.updateActivity(activityID, userId).status)
        }

        @Test
        fun `updating an activity by activity id when it exists, returns 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addActivityResponse = activityUtil.addActivity(activity)

            assertEquals(201, addActivityResponse.status)
            val addedActivity: Activity = jsonNodeToObject(addActivityResponse)

            val updatedActivityResponse = activityUtil.updateActivity(addedActivity.id, addedUser.id)
            assertEquals(204, updatedActivityResponse.status)

            val retrievedActivityResponse = activityUtil.getActivityById(addedActivity.id)
            val updatedActivity: Activity = jsonNodeToObject(retrievedActivityResponse)
            assertEquals(updatedDescription, updatedActivity.description)
            assertEquals(updatedDuration, updatedActivity.duration, 0.1)
            assertEquals(updatedCalories, updatedActivity.calories)
            assertEquals(updatedStarted, updatedActivity.started)

            userUtil.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            assertEquals(404, activityUtil.deleteByActivityId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            assertEquals(404, activityUtil.deleteByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addActivityResponse = activityUtil.addActivity(activity)

            assertEquals(201, addActivityResponse.status)

            val addedActivity: Activity = jsonNodeToObject(addActivityResponse)
            assertEquals(204, activityUtil.deleteByActivityId(addedActivity.id).status)

            userUtil.deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all activities by userid when it exists, returns a 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())

            val addActivityResponse1 = activityUtil.addActivity(activities[0].copy(userId = addedUser.id))
            val addActivityResponse2 = activityUtil.addActivity(activities[1].copy(userId = addedUser.id))
            val addActivityResponse3 = activityUtil.addActivity(activities[2].copy(userId = addedUser.id))

            assertEquals(201, addActivityResponse1.status)
            assertEquals(201, addActivityResponse2.status)
            assertEquals(201, addActivityResponse3.status)

            assertEquals(204, activityUtil.deleteByUserId(addedUser.id).status)

            val addedActivity1: Activity = jsonNodeToObject(addActivityResponse1)
            val addedActivity2: Activity = jsonNodeToObject(addActivityResponse2)
            val addedActivity3: Activity = jsonNodeToObject(addActivityResponse3)

            assertEquals(404, activityUtil.getActivityById(addedActivity1.id).status)
            assertEquals(404, activityUtil.getActivityById(addedActivity2.id).status)
            assertEquals(404, activityUtil.getActivityById(addedActivity3.id).status)

            userUtil.deleteUser(addedUser.id)
        }
    }
}