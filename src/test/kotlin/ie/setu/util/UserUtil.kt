package ie.setu.util

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class UserUtil(private val origin: String) {

    fun addUser(name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    fun updateUser(id: Int, name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    fun deleteUser(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }

    fun retrieveUserByEmail(email: String): HttpResponse<String> {
        return Unirest.get("$origin/api/users/email/$email").asString()
    }

    fun retrieveUserById(id: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/users/$id").asString()
    }
}