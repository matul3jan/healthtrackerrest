package ie.setu.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.Params.USER_EMAIL
import ie.setu.config.Params.USER_ID
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object UserApi : Api {

    override val endpoints = EndpointGroup {
        path("/users") {
            get(::getAllUsers)
            post(::addUser)
            path("{${USER_ID}}") {
                get(::getUserById)
                patch(::updateUser)
                delete(::deleteUser)
            }
            path("email/{${USER_EMAIL}}") {
                get(::getUserByEmail)
            }
        }
    }

    private val mapper = jacksonObjectMapper()

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    fun getAllUsers(ctx: Context) {
        ctx.status(200)
        ctx.json(UserDAO.findAll())
    }

    @OpenApi(
        summary = "Get user by ID",
        operationId = "getUserById",
        tags = ["User"],
        path = "/api/users/{${USER_ID}}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [
            OpenApiResponse("200", [OpenApiContent(User::class)]),
            OpenApiResponse("404")
        ]
    )
    fun getUserById(ctx: Context) {
        val user = UserDAO.finById(parseUserId(ctx))
        if (user != null) {
            ctx.status(200)
            ctx.json(user)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Get user by Email",
        operationId = "getUserByEmail",
        tags = ["User"],
        path = "/api/users/email/{${USER_EMAIL}}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam(USER_EMAIL, Int::class, "The user email")],
        responses = [
            OpenApiResponse("200", [OpenApiContent(User::class)]),
            OpenApiResponse("404")
        ]
    )
    fun getUserByEmail(ctx: Context) {
        val user = UserDAO.findByEmail(ctx.pathParam(USER_EMAIL))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add User",
        operationId = "addUser",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.POST,
        requestBody = OpenApiRequestBody([OpenApiContent(User::class)]),
        responses = [OpenApiResponse("201")]
    )
    fun addUser(ctx: Context) {
        val user = mapper.readValue<User>(ctx.body())
        UserDAO.save(user)
        ctx.status(201)
    }

    @OpenApi(
        summary = "Delete user by ID",
        operationId = "deleteUserById",
        tags = ["User"],
        path = "/api/users/{${USER_ID}}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [
            OpenApiResponse("204"),
            OpenApiResponse("404")
        ]
    )
    fun deleteUser(ctx: Context) {
        val id = UserDAO.delete(parseUserId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Update user by ID",
        operationId = "updateUserById",
        tags = ["User"],
        path = "/api/users/{${USER_ID}}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [
            OpenApiResponse("204"),
            OpenApiResponse("404")
        ]
    )
    fun updateUser(ctx: Context) {
        val newUser = mapper.readValue<User>(ctx.body())
        val id = UserDAO.update(parseUserId(ctx), newUser)
        if (id != 0) {
            ctx.status(204)
            ctx.json(newUser)
        } else {
            ctx.status(404)
        }
    }

    private fun parseUserId(ctx: Context) = ctx.pathParam(USER_ID).toInt()
}