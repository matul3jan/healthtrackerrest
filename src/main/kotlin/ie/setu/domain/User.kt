package ie.setu.domain

import java.math.BigDecimal

data class User(
    var id: Int,
    var name: String,
    var email: String,
    var age: Int,
    var gender: String,
    var height: Int,
    var weight: BigDecimal
)