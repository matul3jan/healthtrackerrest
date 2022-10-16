package ie.setu.config

import ie.setu.api.Api.Companion.ApiFactory
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.JavalinConfig
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import org.eclipse.jetty.http.HttpStatus

object JavalinConfig {

    fun startService() {

        val app = Javalin.create(::registerPlugins)

        registerRoutes(app)
        handleErrors(app)
        handleExceptions(app)
        startApplication(app)
    }

    private fun registerPlugins(config: JavalinConfig) {
        config.registerPlugin(
            OpenApiPlugin(OpenApiOptions(
                Info().apply {
                    title = "Health Tracker REST App"
                    description = "Health Tracker API documentation"
                    version = "1.0"
                }
            ).apply {
                path = "/swagger-docs"  // Swagger Json endpoint
                swagger = SwaggerOptions("/swagger-ui") // Swagger UI endpoint
                reDoc = ReDocOptions("/redoc") //  Redoc endpoint
            })
        )
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

    private fun startApplication(app: Javalin) {
        app.start(System.getenv("PORT")?.toInt() ?: 7001)
    }
}