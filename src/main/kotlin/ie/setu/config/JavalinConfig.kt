package ie.setu.config

import ie.setu.api.Api.Companion.ApiFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import org.eclipse.jetty.http.HttpStatus

object JavalinConfig {

    fun startService() {

        val app = Javalin.create()

        registerRoutes(app)
        handleErrors(app)
        handleExceptions(app)

        app.start(7001)
    }

    private fun registerRoutes(app: Javalin) {
        app.routes { ApiFactory.forEach { path("/api", it.endpoints) } }
    }

    private fun handleErrors(app: Javalin) {
        app.error(HttpStatus.NOT_FOUND_404) { ctx -> ctx.json("Sorry, resource not found!") }
    }

    private fun handleExceptions(app: Javalin) {
        app.exception(Exception::class.java) { e, _ -> e.printStackTrace() }
    }
}