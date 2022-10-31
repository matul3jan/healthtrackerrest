package ie.setu.api

import ie.setu.config.Params.USER_EMAIL
import ie.setu.config.Params.USER_ID
import ie.setu.config.Role
import ie.setu.controller.UserController
import ie.setu.domain.User
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object UserApi : Api {

    const val apiPathUsers = "/api/users"

    private const val tag = "User"

    override val endpoints = EndpointGroup {
        path("/users") {
            get(::getAllUsers)
            post(::addUser, Role.ANYONE)
            path("/{$USER_ID}") {
                get(::getUserById, Role.USER)
                patch(::updateUser, Role.USER)
                delete(::deleteUser, Role.USER)
                // Nested endpoints
                path("/activities") {
                    get(ActivityApi::getActivitiesByUserId, Role.USER)
                    delete(ActivityApi::deleteActivityByUserId, Role.USER)
                }
                path("/goals") {
                    get(GoalApi::getGoalsByUserId, Role.USER)
                    delete(GoalApi::deleteGoalByUserId, Role.USER)
                }
            }
            path("/email/{$USER_EMAIL}") {
                get(::getUserByEmail)
            }
        }
    }

    @OpenApi(
        tags = [tag],
        path = apiPathUsers,
        method = HttpMethod.GET,
        summary = "Get all users",
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    private fun getAllUsers(ctx: Context) = UserController.getAllUsers(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}",
        method = HttpMethod.GET,
        summary = "Get user by ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The User ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(User::class)]), OpenApiResponse("404")]
    )
    private fun getUserById(ctx: Context) = UserController.getUserById(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/email/{$USER_EMAIL}",
        method = HttpMethod.GET,
        summary = "Get user by Email",
        pathParams = [OpenApiParam(USER_EMAIL, Int::class, "The user email")],
        responses = [OpenApiResponse("200", [OpenApiContent(User::class)]), OpenApiResponse("404")]
    )
    private fun getUserByEmail(ctx: Context) = UserController.getUserByEmail(ctx)

    @OpenApi(
        tags = [tag],
        path = apiPathUsers,
        method = HttpMethod.POST,
        summary = "Add User",
        requestBody = OpenApiRequestBody([OpenApiContent(User::class)]),
        responses = [OpenApiResponse("201")]
    )
    private fun addUser(ctx: Context) = UserController.addUser(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}",
        method = HttpMethod.DELETE,
        summary = "Delete user by ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun deleteUser(ctx: Context) = UserController.deleteUser(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}",
        method = HttpMethod.PATCH,
        summary = "Update user by ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The user ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun updateUser(ctx: Context) = UserController.updateUser(ctx)
}