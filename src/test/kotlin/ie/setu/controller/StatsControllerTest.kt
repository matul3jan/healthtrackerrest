package ie.setu.controller

import ie.setu.controller.StatsController.calculateBmi
import ie.setu.controller.StatsController.calculateFluidIntake
import ie.setu.controller.StatsController.calculateIdealWeight
import ie.setu.helpers.users
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StatsControllerTest {
    @Test
    fun `should calculate correct fluid intake person with age less than 30`() {
        assertEquals(11.59, calculateFluidIntake(users[0]), 0.1)
    }

    @Test
    fun `should calculate correct fluid intake person with age between 30 and 55`() {
        assertEquals(10.14, calculateFluidIntake(users[0].copy(age = 45)), 0.1)
    }

    @Test
    fun `should calculate correct fluid intake person with age more than 55`() {
        assertEquals(8.69, calculateFluidIntake(users[0].copy(age = 60)), 0.1)
    }

    @Test
    fun `should calculate correct bmi`() {
        assertEquals(20.24, calculateBmi(users[0]), 0.1)
    }

    @Test
    fun `should calculate correct ideal weight`() {
        assertEquals(70.49, calculateIdealWeight(users[0]), 0.1)
    }

    @Test
    fun `should calculate correct ideal weight for other gender`() {
        assertEquals(users[0].weight.toDouble(), calculateIdealWeight(users[0].copy(gender = "O")), 0.1)
    }
}