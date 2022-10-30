package ie.setu.util

import ie.setu.domain.Goal
import ie.setu.helpers.updatedCurrent
import ie.setu.helpers.updatedTarget
import ie.setu.helpers.updatedUnit
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class GoalUtil(private val origin: String) {

    private val path = "$origin/api/goals"
    private val updatedGoal = Goal(-1, updatedTarget, updatedCurrent, updatedUnit, -1, -1)

    fun getGoals(): HttpResponse<JsonNode> = Unirest.get(path).asJson()

    fun getGoalById(id: Int): HttpResponse<JsonNode> = Unirest.get("$path/$id").asJson()

    fun retrieveByUserId(id: Int): HttpResponse<JsonNode> = Unirest.get("$origin/api/users/$id/goals").asJson()

    fun retrieveByActivityId(id: Int): HttpResponse<JsonNode> = Unirest.get("$origin/api/activities/$id/goals").asJson()

    fun addGoal(goal: Goal): HttpResponse<JsonNode> = Unirest.post(path).body(goal).asJson()

    fun updateGoal(id: Int, userId: Int, activityId: Int): HttpResponse<JsonNode> {
        val goal = updatedGoal.copy(userId = userId, activityId = activityId)
        return Unirest.patch("$path/$id").body(goal).asJson()
    }

    fun deleteByGoalId(id: Int): HttpResponse<JsonNode> = Unirest.delete("$path/$id").asJson()

    fun deleteByUserId(id: Int): HttpResponse<JsonNode> = Unirest.delete("$origin/api/users/$id/goals").asJson()
}