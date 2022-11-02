package ie.setu.controller

import ie.setu.config.Params.parseActivityId
import ie.setu.config.Params.parseGoalId
import ie.setu.config.Params.parseUserId
import ie.setu.domain.Goal
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.GoalDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.JsonUtil
import io.javalin.http.Context

object GoalController {

    fun getAllGoals(ctx: Context) {
        ctx.status(200)
        ctx.json(GoalDAO.getAll())
    }

    fun getGoalById(ctx: Context) {
        val goal = GoalDAO.findById(parseGoalId(ctx))
        if (goal != null) {
            ctx.status(200)
            ctx.json(goal)
        } else {
            ctx.status(404)
        }
    }

    fun getGoalsByUserId(ctx: Context) {
        val userId = parseUserId(ctx)
        if (UserDAO.findById(userId) != null) {
            val goals = GoalDAO.findByUserId(userId)
            ctx.status(200)
            ctx.json(goals)
        } else {
            ctx.status(404)
        }
    }

    fun getGoalsByActivityId(ctx: Context) {
        val goal = GoalDAO.findByActivityId(parseActivityId(ctx))
        if (goal != null) {
            ctx.status(200)
            ctx.json(goal)
        } else {
            ctx.status(404)
        }
    }

    fun addGoal(ctx: Context) {
        val goal: Goal = JsonUtil.jsonToObject(ctx.body())
        val user = UserDAO.findById(goal.userId)
        if (user != null) {
            val activity = ActivityDAO.findById(goal.activityId)
            if (activity != null) {
                val id = GoalDAO.save(goal)
                goal.id = id
                ctx.json(goal)
                ctx.status(201)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun updateGoal(ctx: Context) {
        val newGoal: Goal = JsonUtil.jsonToObject(ctx.body())
        val id = GoalDAO.update(parseGoalId(ctx), newGoal)
        if (id != 0) {
            ctx.status(204)
            ctx.json(newGoal)
        } else {
            ctx.status(404)
        }
    }

    fun deleteGoalById(ctx: Context) {
        val id = GoalDAO.delete(parseGoalId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteGoalByUserId(ctx: Context) {
        val id = GoalDAO.deleteAllForUser(parseUserId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
}