package ie.setu

import ie.setu.config.DBConfig
import ie.setu.config.JavalinConfig

fun main() {
    DBConfig.createDbConnection()
    JavalinConfig.startService()
}