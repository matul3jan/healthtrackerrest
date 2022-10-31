package ie.setu.config

import ie.setu.config.Properties.getProperty
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

object DBConfig {

    private val logger = KotlinLogging.logger {}

    fun createDbConnection() {

        logger.info { "Trying database connection..." }

        val connection = Database.connect(
            url = getProperty("database.url"),
            driver = getProperty("database.driver"),
            user = getProperty("database.user"),
            password = getProperty("database.password")
        )

        logger.info { "database name = ${connection.name}" }
        logger.info { "database url = ${connection.url}" }
    }
}