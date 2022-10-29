package ie.setu.util

import ie.setu.domain.Activity
import ie.setu.helpers.updatedCalories
import ie.setu.helpers.updatedDescription
import ie.setu.helpers.updatedDuration
import ie.setu.helpers.updatedStarted
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class ActivityUtil(private val origin: String) {

    private val path = "$origin/api/activities"
    private val updatedActivity = Activity(-1, updatedDescription, updatedDuration, updatedCalories, updatedStarted, -1)

    fun getActivities(): HttpResponse<JsonNode> = Unirest.get(path).asJson()

    fun getActivityById(id: Int): HttpResponse<JsonNode> = Unirest.get("$path/$id").asJson()

    fun retrieveByUserId(id: Int): HttpResponse<JsonNode> = Unirest.get("$origin/api/users/$id/activities").asJson()

    fun addActivity(activity: Activity): HttpResponse<JsonNode> = Unirest.post(path).body(activity).asJson()

    fun updateActivity(id: Int, userId: Int): HttpResponse<JsonNode> {
        val activity = updatedActivity.copy(userId = userId)
        return Unirest.patch("$path/$id").body(activity).asJson()
    }

    fun deleteByActivityId(id: Int): HttpResponse<JsonNode> = Unirest.delete("$path/$id").asJson()

    fun deleteByUserId(id: Int): HttpResponse<JsonNode> = Unirest.delete("$origin/api/users/$id/activities").asJson()
}