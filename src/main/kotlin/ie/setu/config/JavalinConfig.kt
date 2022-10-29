package ie.setu.config

import ie.setu.api.Api.Companion.ApiFactory
import ie.setu.utils.JsonUtil.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.JavalinConfig
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info
import kong.unirest.Unirest
import kong.unirest.jackson.JacksonObjectMapper
import org.eclipse.jetty.http.HttpStatus

object JavalinConfig {

    fun startService(): Javalin {

        val app = Javalin.create(::setup)

        registerRoutes(app)
        handleErrors(app)
        handleExceptions(app)
        startApplication(app)

        return app
    }

    private fun setup(config: JavalinConfig) {

        // Register OpenAPI Plugin
        config.registerPlugin(openApiPlugin())

        // Register default Json serializer for Javalin and Unirest
        config.jsonMapper(JavalinJackson(jsonObjectMapper()))
        Unirest.config().objectMapper = JacksonObjectMapper(jsonObjectMapper())
    }

    private fun registerRoutes(app: Javalin) {
        app.routes { ApiFactory.forEach { path("/api", it.endpoints) } }
    }

    private fun handleErrors(app: Javalin) {
        app
            .error(HttpStatus.NOT_FOUND_404) { ctx -> ctx.result("404 - Resource not found") }
            .error(HttpStatus.INTERNAL_SERVER_ERROR_500) { ctx -> ctx.result("500 - Internal server error") }
    }

    private fun handleExceptions(app: Javalin) {
        app.exception(Exception::class.java) { _, ctx -> ctx.status(500) }
    }

    private fun startApplication(app: Javalin) {
        app.start(System.getenv("PORT")?.toInt() ?: 7001)
    }

    private fun openApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title = "Health Tracker REST App"
                description = "Health Tracker API documentation"
                version = "1.0"
            }
        ).apply {
            path = "/swagger-docs"                        // Swagger Json endpoint
            swagger = SwaggerOptions("/swagger-ui") // Swagger UI endpoint
            reDoc = ReDocOptions("/redoc")         //  Redoc endpoint
        }
    )
}