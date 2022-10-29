package ie.setu.controller

import ie.setu.config.Params
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.JsonUtil.jsonToObject
import io.javalin.http.Context

object ActivityController {

    fun getAllActivities(ctx: Context) {
        ctx.json(ActivityDAO.getAll())
    }

    fun getActivityById(ctx: Context) {
        val activity = ActivityDAO.findByActivityId(parseActivityId(ctx))
        if (activity != null) {
            ctx.status(200)
            ctx.json(activity)
        } else {
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        ActivityDAO.save(activity)
        ctx.json(activity)
    }

    fun updateActivity(ctx: Context) {
        val newActivity: Activity = jsonToObject(ctx.body())
        val id = ActivityDAO.update(parseActivityId(ctx), newActivity)
        if (id != 0) {
            ctx.status(204)
            ctx.json(newActivity)
        } else {
            ctx.status(404)
        }
    }

    fun deleteActivityById(ctx: Context) {
        val id = ActivityDAO.delete(parseActivityId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    // Public functions used outside

    fun getActivitiesByUserId(ctx: Context) {
        if (UserDAO.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = ActivityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(activities)
            }
        }
    }

    fun deleteActivityByUserId(ctx: Context) {
        val id = ActivityDAO.deleteAllForUser(parseUserId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    // Helper functions

    private fun parseActivityId(ctx: Context) = ctx.pathParam(Params.ACTIVITY_ID).toInt()
    private fun parseUserId(ctx: Context) = ctx.pathParam(Params.USER_ID).toInt()
}