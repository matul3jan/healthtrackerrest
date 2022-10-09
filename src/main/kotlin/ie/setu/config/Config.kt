package ie.setu.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import org.eclipse.jetty.http.HttpStatus

object Config {

    fun startService() {
        Javalin.create().apply {
            routes { Controllers.forEach { path("/api", it.endpoints) } }
            error(HttpStatus.NOT_FOUND_404) { ctx -> ctx.json("Sorry, resource not found!") }
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        }.start(7001)
    }
}