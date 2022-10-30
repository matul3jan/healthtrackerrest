package ie.setu.repository

import ie.setu.domain.Goal
import ie.setu.domain.db.Goals
import ie.setu.domain.repository.GoalDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Retrieving some test data from Fixtures
private val goal1 = goals[0]
private val goal2 = goals[1]
private val goal3 = goals[2]

class GoalDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            connectTempDatabase()
        }
    }

    @Nested
    inner class CreateGoals {

        @Test
        fun `multiple goals added to table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
                assertEquals(goal1, GoalDAO.findById(goal1.id))
                assertEquals(goal2, GoalDAO.findById(goal2.id))
                assertEquals(goal3, GoalDAO.findById(goal3.id))
            }
        }
    }

    @Nested
    inner class ReadGoals {

        @Test
        fun `getting all goals from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
            }
        }

        @Test
        fun `get goal by user id that has no goals, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(0, GoalDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get goal by user id that exists, results in a correct goals returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(goal1, GoalDAO.findByUserId(1)[0])
                assertEquals(goal2, GoalDAO.findByUserId(1)[1])
                assertEquals(goal3, GoalDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all goals over empty table returns none`() {
            transaction {
                SchemaUtils.create(Goals)
                assertEquals(0, GoalDAO.getAll().size)
            }
        }

        @Test
        fun `get goal by activity id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(null, GoalDAO.findByActivityId(4))
            }
        }

        @Test
        fun `get goal by activity id that exists, results in a correct goal returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(goal1, GoalDAO.findByActivityId(1))
                assertEquals(goal2, GoalDAO.findByActivityId(3))
            }
        }

        @Test
        fun `get goal by id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(null, GoalDAO.findById(4))
            }
        }

        @Test
        fun `get goal by id that exists, results in a correct goal returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(goal1, GoalDAO.findById(1))
                assertEquals(goal2, GoalDAO.findById(2))
            }
        }
    }

    @Nested
    inner class UpdateGoals {

        @Test
        fun `updating existing goal in table results in successful update`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                val goal3updated = goal3.copy(target = 10.0, current = 7.0, unit = "minutes")
                GoalDAO.update(goal3updated.id, goal3updated)
                assertEquals(goal3updated, GoalDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existent goal in table results in no updates`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                val goal4updated = Goal(id = 4, current = 0.0, target = 0.0, userId = -1, activityId = -1, unit = "")
                GoalDAO.update(4, goal4updated)
                assertEquals(null, GoalDAO.findById(4))
                assertEquals(3, GoalDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteGoals {

        @Test
        fun `deleting a non-existent goal (by id) in table results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
                GoalDAO.delete(4)
                assertEquals(3, GoalDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing goal (by id) in table results in record being deleted`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
                GoalDAO.delete(goal3.id)
                assertEquals(2, GoalDAO.getAll().size)
            }
        }


        @Test
        fun `deleting goals when none exist for user id results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
                GoalDAO.deleteAllForUser(3)
                assertEquals(3, GoalDAO.getAll().size)
            }
        }

        @Test
        fun `deleting goals when 1 or more exist for user id results in deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                populateGoalsTable()
                assertEquals(3, GoalDAO.getAll().size)
                GoalDAO.deleteAllForUser(1)
                assertEquals(1, GoalDAO.getAll().size)
            }
        }
    }
}