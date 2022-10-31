package ie.setu.helpers

import ie.setu.config.DBConfig
import ie.setu.config.JavalinConfig

object ServerContainer {

    val instance by lazy {
        connectDatabase()
        startServerContainer()
    }

    private fun connectDatabase() = DBConfig.createDbConnection()
    private fun startServerContainer() = JavalinConfig.startService()
}