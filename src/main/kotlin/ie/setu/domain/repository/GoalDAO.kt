package ie.setu.domain.repository

import ie.setu.domain.Goal
import ie.setu.domain.db.Goals
import ie.setu.utils.mapToGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object GoalDAO {

    fun getAll(): List<Goal> = transaction {
        Goals.selectAll().map { mapToGoal(it) }
    }

    fun findById(id: Int): Goal? = transaction {
        Goals.select { Goals.id eq id }
            .map { mapToGoal(it) }
            .firstOrNull()
    }

    fun findByUserId(userId: Int): List<Goal> = transaction {
        Goals.select { Goals.userId eq userId }
            .map { mapToGoal(it) }
    }

    fun findByActivityId(activityId: Int): Goal? = transaction {
        Goals.select { Goals.activityId eq activityId }
            .map { mapToGoal(it) }
            .firstOrNull()
    }

    fun save(goal: Goal): Int = transaction {
        Goals.insert {
            it[target] = goal.target
            it[current] = goal.current
            it[unit] = goal.unit
            it[userId] = goal.userId
            it[activityId] = goal.activityId
        } get Goals.id
    }

    fun update(id: Int, goal: Goal): Int = transaction {
        Goals.update({ Goals.id eq id }) {
            it[target] = goal.target
            it[current] = goal.current
            it[unit] = goal.unit
        }
    }

    fun delete(id: Int): Int = transaction {
        Goals.deleteWhere {
            Goals.id eq id
        }
    }

    fun deleteAllForUser(id: Int): Int = transaction {
        Goals.deleteWhere {
            Goals.userId eq id
        }
    }
}