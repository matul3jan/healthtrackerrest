package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Goals : Table("goals") {
    val id = integer("id").autoIncrement().primaryKey()
    val target = double("target")
    val current = double("current")
    val unit = varchar("unit", 50)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val activityId = integer("activity_id").references(Activities.id, onDelete = ReferenceOption.CASCADE)
}