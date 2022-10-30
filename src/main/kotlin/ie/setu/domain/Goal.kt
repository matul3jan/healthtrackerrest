package ie.setu.domain

data class Goal(
    var id: Int,
    val target: Double,
    val current: Double,
    val unit: String,
    val userId: Int,
    val activityId: Int,
)
