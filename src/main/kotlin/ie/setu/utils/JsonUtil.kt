package ie.setu.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.HttpResponse
import kong.unirest.JsonNode


object JsonUtil {

    // Default application JSON serializer
    fun jsonObjectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(JodaModule())
        .registerModule(KotlinModule.Builder().build())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    // Convert Json string to usable object (any type)
    inline fun <reified T : Any> jsonToObject(json: String): T = jacksonObjectMapper()
        .registerModule(JodaModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .readValue(json)

    // Convert Json HTTP Response to usable object (any type)
    inline fun <reified T : Any> jsonNodeToObject(node: HttpResponse<JsonNode>) = jsonToObject<T>(node.body.toString())
}