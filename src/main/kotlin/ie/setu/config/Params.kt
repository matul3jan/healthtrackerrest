package ie.setu.config

import io.javalin.http.Context

object Params {

    // Users
    const val USER_ID = "user-id"
    const val USER_EMAIL = "user-email"

    fun parseUserId(ctx: Context) = ctx.pathParam(USER_ID).toInt()
    fun parseUserEmail(ctx: Context) = ctx.pathParam(USER_EMAIL)

    // Activities
    const val ACTIVITY_ID = "activity-id"

    fun parseActivityId(ctx: Context) = ctx.pathParam(ACTIVITY_ID).toInt()

    // Goals
    const val GOAL_ID = "goal-id"

    fun parseGoalId(ctx: Context) = ctx.pathParam(GOAL_ID).toInt()
}