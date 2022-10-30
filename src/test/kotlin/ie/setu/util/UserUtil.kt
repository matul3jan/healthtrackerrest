package ie.setu.util

import ie.setu.domain.User
import ie.setu.helpers.*
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class UserUtil(origin: String) {

    private val path = "$origin/api/users"
    private val validUser = User(-1, validName, validEmail, validAge, validGender, validHeight, validWeight)
    private val updatedUser = User(-1, updatedName, updatedEmail, updatedAge, updatedGender, updatedHeight, updatedWeight)

    fun getUsers(): HttpResponse<JsonNode> = Unirest.get(path).asJson()

    fun retrieveUserByEmail(email: String): HttpResponse<JsonNode> = Unirest.get("$path/email/$email").asJson()

    fun retrieveUserById(id: Int): HttpResponse<JsonNode> = Unirest.get("$path/$id").asJson()

    fun addUser(): HttpResponse<JsonNode> = Unirest.post(path).body(validUser).asJson()

    fun updateUser(id: Int): HttpResponse<JsonNode> = Unirest.patch("$path/$id").body(updatedUser).asJson()

    fun deleteUser(id: Int): HttpResponse<JsonNode> = Unirest.delete("$path/$id").asJson()
}