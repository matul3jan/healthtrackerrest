package ie.setu.helpers

import ie.setu.config.Properties.getProperty
import ie.setu.domain.Activity
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Goals
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.GoalDAO
import ie.setu.domain.repository.UserDAO
import kong.unirest.Unirest
import okhttp3.internal.immutableListOf
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime
import java.math.BigDecimal

// Users
const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val validAge = 25
const val validGender = "M"
const val validHeight = 180
const val validPassword = "111"
val validWeight = BigDecimal(65.6)

const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"
const val updatedAge = 30
const val updatedGender = "F"
const val updatedHeight = 175
const val updatedPassword = "222"
val updatedWeight = BigDecimal(71.2)

// Activity
const val updatedDescription = "Updated Description"
const val updatedDuration = 30.0
const val updatedCalories = 945
val updatedStarted: DateTime = DateTime.parse("2020-06-11T05:59:27.258Z")

// Goal
const val updatedTarget = 10.0
const val updatedCurrent = 8.0
const val updatedUnit = "minutes"

fun connectTempDatabase() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
}

fun setBasicAuthForTests() {
    Unirest.config().setDefaultBasicAuth(getProperty("test.user"), getProperty("test.password"))
}

fun clearBasicAuthForTests() {
    Unirest.config().clearDefaultHeaders()
}

val users = immutableListOf(
    User(
        id = 1,
        name = "Alice Wonderland",
        email = "alice@wonderland.com",
        age = 25,
        gender = "F",
        height = 180,
        weight = BigDecimal(65.6),
        password = "123"
    ),
    User(
        id = 2,
        name = "Bob Cat",
        email = "bob@cat.ie",
        age = 20,
        gender = "M",
        height = 160,
        weight = BigDecimal(61.9),
        password = "456"
    ),
    User(
        id = 3,
        name = "Mary Contrary",
        email = "mary@contrary.com",
        age = 39,
        gender = "F",
        height = 178,
        weight = BigDecimal(60),
        password = "789"
    ),
    User(
        id = 4,
        name = "Carol Singer",
        email = "carol@singer.com",
        age = 45,
        gender = "F",
        height = 149,
        weight = BigDecimal(75.16),
        password = "000"
    )
)

val activities = immutableListOf(
    Activity(
        id = 1,
        description = "Running",
        duration = 22.0,
        calories = 230,
        started = DateTime.now(),
        userId = users[0].id
    ),
    Activity(
        id = 2,
        description = "Hopping",
        duration = 10.5,
        calories = 80,
        started = DateTime.now(),
        userId = users[0].id
    ),
    Activity(
        id = 3,
        description = "Walking",
        duration = 12.0,
        calories = 120,
        started = DateTime.now(),
        userId = users[1].id
    )
)

val goals = immutableListOf(
    Goal(id = 1, target = 3.0, current = 1.0, unit = "km", userId = users[0].id, activityId = activities[0].id),
    Goal(id = 2, target = 5.0, current = 2.0, unit = "km", userId = users[0].id, activityId = activities[2].id),
    Goal(id = 3, target = 5.0, current = 2.0, unit = "minutes", userId = users[1].id, activityId = activities[2].id)
)

fun populateUserTable() {
    SchemaUtils.create(Users)
    UserDAO.save(users[0])
    UserDAO.save(users[1])
    UserDAO.save(users[2])
}

fun populateActivityTable() {
    SchemaUtils.create(Activities)
    ActivityDAO.save(activities[0])
    ActivityDAO.save(activities[1])
    ActivityDAO.save(activities[2])
}

fun populateGoalsTable() {
    SchemaUtils.create(Goals)
    GoalDAO.save(goals[0])
    GoalDAO.save(goals[1])
    GoalDAO.save(goals[2])
}