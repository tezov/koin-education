package com.tezov.store.shared.presentation.models

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class TechRoleDomainModelMappingTest {

    @Test
    fun `maps all fields correctly hardcoded`() {
        // Expected
        val expected = TechRoleCardUiModel(
            role = TechRoleDomainModel.JUNIOR_DEVELOPER,
            workerCount = 5,
            avgExperience = 3,
            avgProductivity = 4,
            avgSarcasm = 5,
            avgBurnoutRisk = 6
        )

        // Fixtures
        val role = TechRoleDomainModel.JUNIOR_DEVELOPER

        // Test
        val result = role.toWorkerCardUiModel(
            workerCount = 5,
            avgExperience = LevelDomainModel(3),
            avgProductivity = LevelDomainModel(4),
            avgSarcasm = LevelDomainModel(5),
            avgBurnoutRisk = LevelDomainModel(6)
        )

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `maps all fields correctly random`() {
        repeat(10) {
            // Expected
            val role = TechRoleDomainModel.entries.random()
            val expected = TechRoleCardUiModel(
                role = role,
                workerCount = Random.nextInt(0, 50),
                avgExperience = Random.nextInt(1, 10),
                avgProductivity = Random.nextInt(1, 10),
                avgSarcasm = Random.nextInt(1, 10),
                avgBurnoutRisk = Random.nextInt(1, 10)
            )

            // Fixtures
            val avgExperience = LevelDomainModel(expected.avgExperience!!)
            val avgProductivity = LevelDomainModel(expected.avgProductivity!!)
            val avgSarcasm = LevelDomainModel(expected.avgSarcasm!!)
            val avgBurnoutRisk = LevelDomainModel(expected.avgBurnoutRisk!!)

            // Test
            val result = role.toWorkerCardUiModel(
                workerCount = expected.workerCount,
                avgExperience = avgExperience,
                avgProductivity = avgProductivity,
                avgSarcasm = avgSarcasm,
                avgBurnoutRisk = avgBurnoutRisk
            )

            // Assert
            assertEquals(expected, result)
        }
    }
}