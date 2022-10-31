package ie.setu.api

import ie.setu.api.ActivityApi.apiPathActivity
import ie.setu.api.UserApi.apiPathUsers
import ie.setu.config.Params.ACTIVITY_ID
import ie.setu.config.Params.GOAL_ID
import ie.setu.config.Params.USER_ID
import ie.setu.config.Role
import ie.setu.controller.GoalController
import ie.setu.domain.Goal
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object GoalApi : Api {

    private const val apiPath = "/api/goals"
    private const val tag = "Goal"

    override val endpoints = EndpointGroup {
        path("/goals") {
            get(::getAllGoals)
            post(::addGoal, Role.USER)
            path("/{$GOAL_ID}") {
                get(::getGoalById, Role.USER)
                patch(::updateGoal, Role.USER)
                delete(::deleteGoalById, Role.USER)
            }
        }
    }

    @OpenApi(
        tags = [tag],
        path = apiPath,
        method = HttpMethod.GET,
        summary = "Get all goals",
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Goal>::class)])]
    )
    private fun getAllGoals(ctx: Context) = GoalController.getAllGoals(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPath/{$GOAL_ID}",
        method = HttpMethod.GET,
        summary = "Get goal by ID",
        pathParams = [OpenApiParam(GOAL_ID, Int::class, "The Goal ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Goal::class)]), OpenApiResponse("404")]
    )
    private fun getGoalById(ctx: Context) = GoalController.getGoalById(ctx)

    @OpenApi(
        tags = [tag],
        path = apiPath,
        method = HttpMethod.POST,
        summary = "Add goal",
        requestBody = OpenApiRequestBody([OpenApiContent(Goal::class)]),
        responses = [OpenApiResponse("201")]
    )
    private fun addGoal(ctx: Context) = GoalController.addGoal(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPath/{$GOAL_ID}",
        method = HttpMethod.PATCH,
        summary = "Update goal by ID",
        pathParams = [OpenApiParam(GOAL_ID, Int::class, "The goal ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun updateGoal(ctx: Context) = GoalController.updateGoal(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPath/{$GOAL_ID}",
        method = HttpMethod.DELETE,
        summary = "Delete goal by ID",
        pathParams = [OpenApiParam(GOAL_ID, Int::class, "The goal ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun deleteGoalById(ctx: Context) = GoalController.deleteGoalById(ctx)

    // Public handlers used outside

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}/goals",
        method = HttpMethod.GET,
        summary = "Get all goals by User ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The User ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Goal::class)]), OpenApiResponse("404")]
    )
    fun getGoalsByUserId(ctx: Context) = GoalController.getGoalsByUserId(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathActivity/{$ACTIVITY_ID}/goals",
        method = HttpMethod.GET,
        summary = "Get all goals by Activity ID",
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The Activity ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Goal::class)]), OpenApiResponse("404")]
    )
    fun getGoalsByActivityId(ctx: Context) = GoalController.getGoalsByActivityId(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}/goals",
        method = HttpMethod.DELETE,
        summary = "Delete all goals by User ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The User ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Goal::class)]), OpenApiResponse("404")]
    )
    fun deleteGoalByUserId(ctx: Context) = GoalController.deleteGoalByUserId(ctx)
}