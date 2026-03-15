package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.WorkResultDomainModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class WorkResultDataModelLMappingTest {

    @Test
    fun `maps all fields correctly hardcoded`() {
        // Expected
        val expected = WorkResultDomainModel(
            deliveredFeatures = 1,
            bugsIntroduced = 2,
            techDebtCreated = 3
        )

        // Fixtures
        val dataModel = WorkResultDataModel(
            deliveredFeatures = 1,
            bugsIntroduced = 2,
            techDebtCreated = 3
        )

        // Test
        val result = dataModel.toWorkResultDomainModel()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `maps all fields correctly random`() {
        repeat(10) {
            // Expected
            val expected = WorkResultDomainModel(
                deliveredFeatures = Random.nextInt(0, 50),
                bugsIntroduced = Random.nextInt(0, 50),
                techDebtCreated = Random.nextInt(0, 50)
            )

            // Fixtures
            val dataModel = WorkResultDataModel(
                deliveredFeatures = expected.deliveredFeatures,
                bugsIntroduced = expected.bugsIntroduced,
                techDebtCreated = expected.techDebtCreated
            )

            // Test
            val result = dataModel.toWorkResultDomainModel()

            // Assert
            assertEquals(expected, result)
        }
    }

}