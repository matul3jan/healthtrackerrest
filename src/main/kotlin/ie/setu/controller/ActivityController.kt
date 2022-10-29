package ie.setu.controller

import ie.setu.config.Params.parseActivityId
import ie.setu.config.Params.parseUserId
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.JsonUtil.jsonToObject
import io.javalin.http.Context

object ActivityController {

    fun getAllActivities(ctx: Context) {
        ctx.status(200)
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
        val userId = UserDAO.findById(activity.userId)
        if (userId != null) {
            val id = ActivityDAO.save(activity)
            activity.id = id
            ctx.json(activity)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
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
            ctx.status(200)
            ctx.json(activities)
        } else {
            ctx.status(404)
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
}