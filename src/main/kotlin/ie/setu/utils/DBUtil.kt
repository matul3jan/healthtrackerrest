package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Goal
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Goals
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import org.mindrot.BCrypt

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    age = it[Users.age],
    gender = it[Users.gender],
    height = it[Users.height],
    weight = it[Users.weight],
    password = it[Users.password]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToGoal(it: ResultRow) = Goal(
    id = it[Goals.id],
    target = it[Goals.target],
    current = it[Goals.current],
    unit = it[Goals.unit],
    userId = it[Goals.userId],
    activityId = it[Goals.activityId]
)

fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

fun checkPassword(candidate: String, hashed: String): Boolean = BCrypt.checkpw(candidate, hashed)