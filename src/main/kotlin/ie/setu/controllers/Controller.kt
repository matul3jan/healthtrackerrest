package ie.setu.controllers

import io.javalin.apibuilder.EndpointGroup

interface Controller {
    val endpoints: EndpointGroup
}