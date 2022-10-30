package ie.setu.api

import ie.setu.config.DBConfig
import ie.setu.domain.Activity
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.util.ActivityUtil
import ie.setu.util.GoalUtil
import ie.setu.util.UserUtil
import ie.setu.utils.JsonUtil.jsonNodeToObject
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoalApiTest {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    private val goalUtil = GoalUtil(origin)
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
    inner class ReadGoals {

        @Test
        fun `get all goals from the database returns 200 response`() {
            val response = goalUtil.getGoals()
            Assertions.assertEquals(200, response.status)
        }

        @Test
        fun `get goal by id when goal does not exist returns 404 response`() {
            val retrieveResponse = goalUtil.getGoalById(-1)
            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get goal by goal id when goal exists returns 200 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addedActivity: Activity = jsonNodeToObject(activityUtil.addActivity(activity))

            val goal = goals[0].copy(userId = addedUser.id, activityId = addedActivity.id)
            val addedGoalResponse = goalUtil.addGoal(goal)

            Assertions.assertEquals(201, addedGoalResponse.status)

            val addedGoal: Goal = jsonNodeToObject(addedGoalResponse)
            val response = goalUtil.getGoalById(addedGoal.id)

            Assertions.assertEquals(200, response.status)

            Assertions.assertEquals(204, activityUtil.deleteByActivityId(addedActivity.id).status)
            Assertions.assertEquals(204, userUtil.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all goals by user id when no user exists returns 404 response`() {
            val response = goalUtil.retrieveByUserId(-1)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get all goals by user id when user and goals exists returns 200 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addedActivity: Activity = jsonNodeToObject(activityUtil.addActivity(activity))

            goalUtil.addGoal(goals[0].copy(userId = addedUser.id, activityId = addedActivity.id))
            goalUtil.addGoal(goals[1].copy(userId = addedUser.id, activityId = addedActivity.id))
            goalUtil.addGoal(goals[2].copy(userId = addedUser.id, activityId = addedActivity.id))

            val response = goalUtil.retrieveByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)

            val retrievedGoals: Array<Goal> = jsonNodeToObject(response)
            Assertions.assertEquals(3, retrievedGoals.size)

            Assertions.assertEquals(204, activityUtil.deleteByActivityId(addedActivity.id).status)
            Assertions.assertEquals(204, userUtil.deleteUser(addedUser.id).status)
        }
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `add an goal when a user exists for it, returns a 201 response`() {
            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addActivity: Activity = jsonNodeToObject(activityUtil.addActivity(activity))

            val goal = goals[0].copy(userId = addedUser.id, activityId = addActivity.id)
            val response = goalUtil.addGoal(goal)
            Assertions.assertEquals(201, response.status)

            activityUtil.deleteByActivityId(addActivity.id)
            userUtil.deleteUser(addedUser.id)
        }

        @Test
        fun `add an goal when a user exists but no activity exists for it, returns a 404 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())

            val goal = goals[0].copy(userId = addedUser.id, activityId = -1)

            val response = goalUtil.addGoal(goal)
            Assertions.assertEquals(404, response.status)

            userUtil.deleteUser(addedUser.id)
        }

        @Test
        fun `add an goal when no user exists for it, returns a 404 response`() {

            val userId = -1
            Assertions.assertEquals(404, goalUtil.retrieveByUserId(userId).status)

            val activityId = -1
            Assertions.assertEquals(404, goalUtil.retrieveByActivityId(activityId).status)

            val goal = goals[0].copy(userId = userId, activityId = activityId)

            val addedGoal = goalUtil.addGoal(goal)
            Assertions.assertEquals(404, addedGoal.status)
        }
    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating an goal by goal id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1
            val goalID = -1
            Assertions.assertEquals(404, goalUtil.retrieveByUserId(userId).status)
            Assertions.assertEquals(404, goalUtil.retrieveByActivityId(activityID).status)
            Assertions.assertEquals(404, goalUtil.updateGoal(goalID, userId, activityID).status)
        }

        @Test
        fun `updating an goal by goal id when it exists, returns 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addedActivity: Activity = jsonNodeToObject(activityUtil.addActivity(activity))

            val goal = goals[0].copy(userId = addedUser.id, activityId = addedActivity.id)
            val addedGoalResponse = goalUtil.addGoal(goal)

            Assertions.assertEquals(201, addedGoalResponse.status)
            val addedGoal: Goal = jsonNodeToObject(addedGoalResponse)

            val updatedGoalResponse = goalUtil.updateGoal(addedGoal.id, addedActivity.id, addedUser.id)
            Assertions.assertEquals(204, updatedGoalResponse.status)

            val retrievedGoalResponse = goalUtil.getGoalById(addedGoal.id)
            val updatedGoal: Goal = jsonNodeToObject(retrievedGoalResponse)

            Assertions.assertEquals(updatedTarget, updatedGoal.target, 0.1)
            Assertions.assertEquals(updatedCurrent, updatedGoal.current, 0.1)
            Assertions.assertEquals(updatedUnit, updatedGoal.unit)

            activityUtil.deleteByActivityId(addedActivity.id)
            userUtil.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting a goal by goal id when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, goalUtil.deleteByGoalId(-1).status)
        }

        @Test
        fun `deleting goals by user id when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, goalUtil.deleteByUserId(-1).status)
        }

        @Test
        fun `deleting a goal by id when it exists, returns a 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())
            val activity = activities[0].copy(userId = addedUser.id)
            val addedActivity: Activity = jsonNodeToObject(activityUtil.addActivity(activity))

            val goal = goals[0].copy(userId = addedUser.id, activityId = addedActivity.id)
            val addedGoalResponse = goalUtil.addGoal(goal)

            Assertions.assertEquals(201, addedGoalResponse.status)

            val addedGoal: Goal = jsonNodeToObject(addedGoalResponse)
            Assertions.assertEquals(204, goalUtil.deleteByGoalId(addedGoal.id).status)

            activityUtil.deleteByActivityId(addedActivity.id)
            userUtil.deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all goals by userid when it exists, returns a 204 response`() {

            val addedUser: User = jsonNodeToObject(userUtil.addUser())

            val addedActivity1: Activity = jsonNodeToObject(activityUtil.addActivity(activities[0].copy(userId = addedUser.id)))
            val addedActivity2: Activity = jsonNodeToObject(activityUtil.addActivity(activities[1].copy(userId = addedUser.id)))
            val addedActivity3: Activity = jsonNodeToObject(activityUtil.addActivity(activities[2].copy(userId = addedUser.id)))

            val addedGoalResponse1 = goalUtil.addGoal(goals[0].copy(userId = addedUser.id, activityId = addedActivity1.id))
            val addedGoalResponse2 = goalUtil.addGoal(goals[1].copy(userId = addedUser.id, activityId = addedActivity2.id))
            val addedGoalResponse3 = goalUtil.addGoal(goals[2].copy(userId = addedUser.id, activityId = addedActivity3.id))

            Assertions.assertEquals(201, addedGoalResponse1.status)
            Assertions.assertEquals(201, addedGoalResponse2.status)
            Assertions.assertEquals(201, addedGoalResponse3.status)

            Assertions.assertEquals(204, goalUtil.deleteByUserId(addedUser.id).status)

            val addedGoal1: Goal = jsonNodeToObject(addedGoalResponse1)
            val addedGoal2: Goal = jsonNodeToObject(addedGoalResponse2)
            val addedGoal3: Goal = jsonNodeToObject(addedGoalResponse3)

            Assertions.assertEquals(404, goalUtil.getGoalById(addedGoal1.id).status)
            Assertions.assertEquals(404, goalUtil.getGoalById(addedGoal2.id).status)
            Assertions.assertEquals(404, goalUtil.getGoalById(addedGoal3.id).status)

            activityUtil.deleteByActivityId(addedActivity1.id)
            activityUtil.deleteByActivityId(addedActivity2.id)
            activityUtil.deleteByActivityId(addedActivity3.id)
            userUtil.deleteUser(addedUser.id)
        }
    }
}