package ie.setu.config

import ie.setu.App
import java.util.Properties

object Properties {

    private val props = Properties().apply {
        load(App::class.java.classLoader.getResourceAsStream("application.properties"))
    }

    fun getProperty(property: String): String = props.getProperty(property)
}
