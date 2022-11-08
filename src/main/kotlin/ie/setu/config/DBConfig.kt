package ie.setu.config

import ie.setu.config.Properties.getProperty
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database

object DBConfig {

    private val logger = KotlinLogging.logger {}

    fun createDbConnection() {

        logger.info { "Trying database connection..." }

        val dbConfig = Database.connect(
            url = getProperty("database.url"),
            driver = getProperty("database.driver"),
            user = getProperty("database.user"),
            password = getProperty("database.password")
        )

        logger.info { "db url - connection: " + dbConfig.url }
    }
}