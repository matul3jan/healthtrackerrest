package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.activities
import ie.setu.helpers.connectTempDatabase
import ie.setu.helpers.populateActivityTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Retrieving some test data from Fixtures
private val activity1 = activities[0]
private val activity2 = activities[1]
private val activity3 = activities[2]

class ActivityDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            connectTempDatabase()
        }
    }

    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
                assertEquals(activity1, ActivityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, ActivityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, ActivityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `getting all activities from a populated table returns all rows`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
            }
        }

        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(0, ActivityDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get activity by user id that exists, results in a correct activities returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(activity1, ActivityDAO.findByUserId(1)[0])
                assertEquals(activity2, ActivityDAO.findByUserId(1)[1])
                assertEquals(activity3, ActivityDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {
                SchemaUtils.create(Activities)
                assertEquals(0, ActivityDAO.getAll().size)
            }
        }

        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(null, ActivityDAO.findByActivityId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(activity1, ActivityDAO.findByActivityId(1))
                assertEquals(activity3, ActivityDAO.findByActivityId(3))
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val activity3updated = Activity(
                    id = 3, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2
                )
                ActivityDAO.update(activity3updated.id, activity3updated)
                assertEquals(activity3updated, ActivityDAO.findByActivityId(3))
            }
        }

        @Test
        fun `updating non-existent activity in table results in no updates`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                val activity4updated = Activity(
                    id = 4, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2
                )
                ActivityDAO.update(4, activity4updated)
                assertEquals(null, ActivityDAO.findByActivityId(4))
                assertEquals(3, ActivityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existent activity (by id) in table results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
                ActivityDAO.delete(4)
                assertEquals(3, ActivityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
                ActivityDAO.delete(activity3.id)
                assertEquals(2, ActivityDAO.getAll().size)
            }
        }


        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
                ActivityDAO.deleteAllForUser(3)
                assertEquals(3, ActivityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {
                populateUserTable()
                populateActivityTable()
                assertEquals(3, ActivityDAO.getAll().size)
                ActivityDAO.deleteAllForUser(1)
                assertEquals(1, ActivityDAO.getAll().size)
            }
        }
    }
}
