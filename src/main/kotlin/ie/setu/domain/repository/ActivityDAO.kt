package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object ActivityDAO {

    fun getAll(): List<Activity> = transaction {
        Activities.selectAll().map { mapToActivity(it) }
    }

    fun findById(id: Int): Activity? = transaction {
        Activities.select { Activities.id eq id }
            .map { mapToActivity(it) }
            .firstOrNull()
    }

    fun findByUserId(userId: Int): List<Activity> = transaction {
        Activities.select { Activities.userId eq userId }
            .map { mapToActivity(it) }
    }

    fun save(activity: Activity): Int = transaction {
        Activities.insert {
            it[description] = activity.description
            it[duration] = activity.duration
            it[started] = activity.started
            it[calories] = activity.calories
            it[userId] = activity.userId
        } get Activities.id
    }

    fun update(id: Int, activity: Activity): Int = transaction {
        Activities.update({ Activities.id eq id }) {
            it[description] = activity.description
            it[duration] = activity.duration
            it[started] = activity.started
            it[calories] = activity.calories
        }
    }

    fun delete(id: Int): Int = transaction {
        Activities.deleteWhere {
            Activities.id eq id
        }
    }

    fun deleteAllForUser(id: Int): Int = transaction {
        Activities.deleteWhere {
            Activities.userId eq id
        }
    }
}