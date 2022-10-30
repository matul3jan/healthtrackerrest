package ie.setu.api

import io.javalin.apibuilder.EndpointGroup

interface Api {

    val endpoints: EndpointGroup

    companion object {
        val ApiFactory = arrayListOf(
            UserApi,
            ActivityApi,
            GoalApi
        )
    }
}