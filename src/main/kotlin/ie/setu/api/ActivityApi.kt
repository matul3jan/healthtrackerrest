package ie.setu.api

import ie.setu.api.UserApi.apiPathUsers
import ie.setu.config.Params.ACTIVITY_ID
import ie.setu.config.Params.USER_ID
import ie.setu.controller.ActivityController
import ie.setu.domain.Activity
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object ActivityApi : Api {

    const val apiPathActivity = "/api/activities"

    private const val tag = "Activity"

    override val endpoints = EndpointGroup {
        path("/activities") {
            get(::getAllActivities)
            post(::addActivity)
            path("/{$ACTIVITY_ID}") {
                get(::getActivityById)
                patch(::updateActivity)
                delete(::deleteActivityById)
                // Nested endpoint
                path("/goals") {
                    get(GoalApi::getGoalsByActivityId)
                }
            }
        }
    }

    @OpenApi(
        tags = [tag],
        path = apiPathActivity,
        method = HttpMethod.GET,
        summary = "Get all activities",
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)])]
    )
    private fun getAllActivities(ctx: Context) = ActivityController.getAllActivities(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathActivity/{$ACTIVITY_ID}",
        method = HttpMethod.GET,
        summary = "Get activity by ID",
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The Activity ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Activity::class)]), OpenApiResponse("404")]
    )
    private fun getActivityById(ctx: Context) = ActivityController.getActivityById(ctx)

    @OpenApi(
        tags = [tag],
        path = apiPathActivity,
        method = HttpMethod.POST,
        summary = "Add Activity",
        requestBody = OpenApiRequestBody([OpenApiContent(Activity::class)]),
        responses = [OpenApiResponse("201")]
    )
    private fun addActivity(ctx: Context) = ActivityController.addActivity(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathActivity/{$ACTIVITY_ID}",
        method = HttpMethod.PATCH,
        summary = "Update activity by ID",
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The activity ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun updateActivity(ctx: Context) = ActivityController.updateActivity(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathActivity/{$ACTIVITY_ID}",
        method = HttpMethod.DELETE,
        summary = "Delete activity by ID",
        pathParams = [OpenApiParam(ACTIVITY_ID, Int::class, "The activity ID")],
        responses = [OpenApiResponse("204"), OpenApiResponse("404")]
    )
    private fun deleteActivityById(ctx: Context) = ActivityController.deleteActivityById(ctx)

    // Public handlers used outside

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}/activities",
        method = HttpMethod.GET,
        summary = "Get all activities by User ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The User ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Activity::class)]), OpenApiResponse("404")]
    )
    fun getActivitiesByUserId(ctx: Context) = ActivityController.getActivitiesByUserId(ctx)

    @OpenApi(
        tags = [tag],
        path = "$apiPathUsers/{$USER_ID}/activities",
        method = HttpMethod.DELETE,
        summary = "Delete all activities by User ID",
        pathParams = [OpenApiParam(USER_ID, Int::class, "The User ID")],
        responses = [OpenApiResponse("200", [OpenApiContent(Activity::class)]), OpenApiResponse("404")]
    )
    fun deleteActivityByUserId(ctx: Context) = ActivityController.deleteActivityByUserId(ctx)
}