package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import okhttp3.internal.immutableListOf
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

// Users
const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"

// Activity
const val updatedDescription = "Updated Description"
const val updatedDuration = 30.0
const val updatedCalories = 945
val updatedStarted: DateTime = DateTime.parse("2020-06-11T05:59:27.258Z")

fun connectTempDatabase() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
}

val users = immutableListOf(
    User(id = 1, name = "Alice Wonderland", email = "alice@wonderland.com"),
    User(id = 2, name = "Bob Cat", email = "bob@cat.ie"),
    User(id = 3, name = "Mary Contrary", email = "mary@contrary.com"),
    User(id = 4, name = "Carol Singer", email = "carol@singer.com")
)

val activities = immutableListOf(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
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