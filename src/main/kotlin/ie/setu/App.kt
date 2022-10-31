package ie.setu

import ie.setu.config.DBConfig
import ie.setu.config.JavalinConfig

class App

fun main() {
    DBConfig.createDbConnection()
    JavalinConfig.startService()
}