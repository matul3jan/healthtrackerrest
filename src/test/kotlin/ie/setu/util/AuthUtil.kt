package ie.setu.util

import ie.setu.config.Properties.getProperty
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class AuthUtil(private val origin: String) {

    fun openSwaggerUi(): HttpResponse<JsonNode> = Unirest.get("$origin${getProperty("swagger.ui.url")}").asJson()
    fun openSwaggerDocs(): HttpResponse<JsonNode> = Unirest.get("$origin${getProperty("swagger.docs.url")}").asJson()
    fun openRedoc(): HttpResponse<JsonNode> = Unirest.get("$origin${getProperty("redoc.url")}").asJson()

    fun getAllUsers(): HttpResponse<JsonNode> = Unirest.get("$origin/api/users").asJson()
    fun getAllActivities(): HttpResponse<JsonNode> = Unirest.get("$origin/api/activities").asJson()
    fun getAllGoals(): HttpResponse<JsonNode> = Unirest.get("$origin/api/goals").asJson()

    fun getAllUsersAsAdmin(): HttpResponse<JsonNode> = getAsAdmin("$origin/api/users")
    fun getAllActivitiesAsAdmin(): HttpResponse<JsonNode> = getAsAdmin("$origin/api/activities")
    fun getAllGoalsAsAdmin(): HttpResponse<JsonNode> = getAsAdmin("$origin/api/goals")

    fun getAllUsersAsTester(): HttpResponse<JsonNode> = getAsTester("$origin/api/users")
    fun getAllActivitiesAsTester(): HttpResponse<JsonNode> = getAsTester("$origin/api/activities")
    fun getAllGoalsAsTester(): HttpResponse<JsonNode> = getAsTester("$origin/api/goals")

    private fun getAsAdmin(url: String) =
        Unirest.get(url).basicAuth(getProperty("admin.user"), getProperty("admin.password")).asJson()

    private fun getAsTester(url: String) =
        Unirest.get(url).basicAuth(getProperty("test.user"), getProperty("test.password")).asJson()
}
