package ie.setu.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.Params.USER_EMAIL
import ie.setu.config.Params.USER_ID
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object UserController {

    private val mapper = jacksonObjectMapper()

    fun getAllUsers(ctx: Context) {
        ctx.status(200)
        ctx.json(UserDAO.findAll())
    }

    fun getUserById(ctx: Context) {
        val user = UserDAO.findById(parseUserId(ctx))
        if (user != null) {
            ctx.status(200)
            ctx.json(user)
        } else {
            ctx.status(404)
        }
    }

    fun getUserByEmail(ctx: Context) {
        val user = UserDAO.findByEmail(ctx.pathParam(USER_EMAIL))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addUser(ctx: Context) {
        val user = mapper.readValue<User>(ctx.body())
        UserDAO.save(user)
        ctx.status(201)
    }

    fun deleteUser(ctx: Context) {
        val id = UserDAO.delete(parseUserId(ctx))
        if (id != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

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