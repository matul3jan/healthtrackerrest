package ie.setu.controller

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.Params
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object ActivityController {
    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(ActivityDAO.getAll()))
    }

    fun getActivityById(ctx: Context) {
        val activity = ActivityDAO.findByActivityId(parseActivityId(ctx))
        if (activity != null) {
            ctx.status(200)
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activity))
        } else {
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        ActivityDAO.save(activity)
        ctx.json(mapper.writeValueAsString(activity))
    }

    fun updateActivity(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val newActivity = mapper.readValue<Activity>(ctx.body())
        val id = ActivityDAO.update(parseActivityId(ctx), newActivity)
        if (id != 0) {
            ctx.status(204)
            ctx.json(mapper.writeValueAsString(newActivity))
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
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
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