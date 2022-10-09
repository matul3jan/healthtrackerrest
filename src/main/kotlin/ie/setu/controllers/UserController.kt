package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.Params
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context

object UserController : Controller {

    override val endpoints = EndpointGroup {
        path("users") {
            get(::getUsers)
            post(::addUser)
            path("{user-id}") {
                get(::getUserById)
                patch(::updateUser)
                delete(::deleteUser)
            }
            path("email/{user-email}") {
                get(::getUserByEmail)
            }
        }
    }

    private val userDAO = UserDAO()
    private val mapper = jacksonObjectMapper()

    private fun getUsers(ctx: Context) = ctx.json(userDAO.findAll())

    private fun getUserById(ctx: Context) {
        val user = userDAO.finById(parseUserId(ctx))
        if (user != null) {
            ctx.json(user)
        }
    }

    private fun getUserByEmail(ctx: Context) {
        val user = userDAO.findByEmail(ctx.pathParam(Params.USER_EMAIL))
        if (user != null) {
            ctx.json(user)
        }
    }

    private fun addUser(ctx: Context) {
        val user = mapper.readValue<User>(ctx.body())
        userDAO.save(user)
        ctx.json(userDAO)
    }

    private fun deleteUser(ctx: Context) {
        val removed = userDAO.delete(parseUserId(ctx))
        if (removed) {
            ctx.status(204)
        }
    }

    private fun updateUser(ctx: Context) {
        val user = userDAO.finById(parseUserId(ctx))
        if (user != null) {
            val newUser = mapper.readValue<User>(ctx.body())
            userDAO.update(user, newUser)
            ctx.json(user)
        }
    }

    private fun parseUserId(ctx: Context) = ctx.pathParam(Params.USER_ID).toInt()
}