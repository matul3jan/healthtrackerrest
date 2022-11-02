package ie.setu.controller

import ie.setu.config.Params
import ie.setu.domain.User
import ie.setu.domain.UserStats
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import java.text.DecimalFormat

object StatsController {

    private val df = DecimalFormat("#.##")

    fun getUserStats(ctx: Context) {
        val user = UserDAO.findById(Params.parseUserId(ctx))
        if (user != null) {
            val stats = UserStats(
                df.format(calculateBmi(user)),
                df.format(calculateIdealWeight(user)),
                df.format(calculateFluidIntake(user))
            )
            ctx.status(200)
            ctx.json(stats)
        } else {
            ctx.status(404)
        }
    }

    /**
     * Calculate fluid intake in cups based on weight and age.
     */
    fun calculateFluidIntake(user: User): Double {
        var weight = user.weight.toDouble()
        when {
            user.age < 30 -> weight *= 40
            user.age < 55 -> weight *= 35
            user.age > 55 -> weight *= 30
        }
        return (weight / 28.3) / 8
    }

    /**
     * Calculate body mass index using formula bmi = w / (h * h)
     * w : weight in kilogram
     * h : height in metre
     */
    fun calculateBmi(user: User): Double {
        val weight = user.weight.toDouble()
        val height = user.height * 0.01 // convert from centimetre to metre
        return weight / (height * height)
    }

    /**
     * Calculate ideal body weight in kg using Devine formula
     */
    fun calculateIdealWeight(user: User): Double {
        val threshold = 152.4
        val height = user.height
        val extraCms = if (height > threshold) height - threshold else 0.0
        val extraInches = extraCms * 0.393701
        return when (user.gender) {
            "M" -> 50.0 + (2.3 * extraInches)
            "F" -> 45.5 + (2.3 * extraInches)
            else -> user.weight.toDouble()
        }
    }
}