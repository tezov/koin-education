package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class TechWorkerDataModelMappingTest {

    @Test
    fun `maps all fields correctly hardcoded`() {
        // Expected
        val expected = TechWorkerDomainModel(
            id = WorkerIdDomainModel("id-1"),
            name = "name-1",
            role = TechRoleDataModel.JUNIOR_DEVELOPER.toTechRoleDomainModel(),
            experience = LevelDomainModel(1),
            productivity = LevelDomainModel(2),
            sarcasmLevel = LevelDomainModel(3),
            burnoutRisk = LevelDomainModel(4)
        )

        // Fixtures
        val dataModel = TechWorkerDataModel(
            id = "id-1",
            name = "name-1",
            role = TechRoleDataModel.JUNIOR_DEVELOPER,
            experience = 1,
            productivity = 2,
            sarcasmLevel = 3,
            burnoutRisk = 4,
            burnoutReport = BurnoutReportDataModel(
                severity = 1,
                reason = "reason"
            ),
            workResult = WorkResultDataModel(
                deliveredFeatures = 1,
                bugsIntroduced = 2,
                techDebtCreated = 3
            )
        )

        // Test
        val result = dataModel.toTechWorkerDomainModel()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `maps all fields correctly random`() {
        repeat(10) {
            val id = "id-${Random.nextInt(0, 1000)}"
            val name = "name-${Random.nextInt(0, 1000)}"
            val role = TechRoleDataModel.entries.random()

            val experience = Random.nextInt(1, 10)
            val productivity = Random.nextInt(1, 10)
            val sarcasmLevel = Random.nextInt(1, 10)
            val burnoutRisk = Random.nextInt(1, 10)

            // Expected
            val expected = TechWorkerDomainModel(
                id = WorkerIdDomainModel(id),
                name = name,
                role = role.toTechRoleDomainModel(),
                experience = LevelDomainModel(experience),
                productivity = LevelDomainModel(productivity),
                sarcasmLevel = LevelDomainModel(sarcasmLevel),
                burnoutRisk = LevelDomainModel(burnoutRisk)
            )

            // Fixtures
            val dataModel = TechWorkerDataModel(
                id = id,
                name = name,
                role = role,
                experience = experience,
                productivity = productivity,
                sarcasmLevel = sarcasmLevel,
                burnoutRisk = burnoutRisk,
                burnoutReport = BurnoutReportDataModel(
                    severity = Random.nextInt(0, 10),
                    reason = "reason-${Random.nextInt(0, 1000)}"
                ),
                workResult = WorkResultDataModel(
                    deliveredFeatures = Random.nextInt(0, 50),
                    bugsIntroduced = Random.nextInt(0, 50),
                    techDebtCreated = Random.nextInt(0, 50)
                )
            )

            // Test
            val result = dataModel.toTechWorkerDomainModel()

            // Assert
            assertEquals(expected, result)
        }
    }

}