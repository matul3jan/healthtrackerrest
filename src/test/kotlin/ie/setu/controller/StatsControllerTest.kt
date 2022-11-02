package ie.setu.controller

import ie.setu.controller.StatsController.calculateBmi
import ie.setu.controller.StatsController.calculateFluidIntake
import ie.setu.controller.StatsController.calculateIdealWeight
import ie.setu.helpers.users
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StatsControllerTest {
    @Test
    fun `should calculate correct fluid intake`() {
        assertEquals(11.59, calculateFluidIntake(users[0]), 0.1)
    }

    @Test
    fun `should calculate correct bmi`() {
        assertEquals(20.24, calculateBmi(users[0]), 0.1)
    }

    @Test
    fun `should calculate correct ideal weight`() {
        assertEquals(70.49, calculateIdealWeight(users[0]), 0.1)
    }
}