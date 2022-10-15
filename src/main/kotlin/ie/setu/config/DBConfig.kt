package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

object DBConfig {

    private val logger = KotlinLogging.logger {}

    fun createDbConnection() {

        logger.info{"Trying database connection..."}

        val connection = Database.connect(
            "jdbc:postgresql://ec2-34-194-40-194.compute-1.amazonaws.com:5432/d46ijni476a53o?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "nkzonqdqjisorb",
            password = "152148121807b9fc84dad89b2eb0fc86d365a958b21ecb3bb899f8b382d3300e"
        )

        logger.info{"database name = " + connection.name}
        logger.info{"database url = " + connection.url}
    }
}