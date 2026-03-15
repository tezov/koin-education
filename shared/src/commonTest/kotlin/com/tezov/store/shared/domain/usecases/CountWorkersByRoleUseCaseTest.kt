package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CountWorkersByRoleUseCaseTest {

    private lateinit var listWorkersByRoleUseCase: ListWorkersByRoleUseCase

    private lateinit var sut: CountWorkersByRoleUseCase

    @BeforeTest
    fun setUp() {
        listWorkersByRoleUseCase = mock()
        sut = CountWorkersByRoleUseCase(listWorkersByRoleUseCase)
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(listWorkersByRoleUseCase)
    }

    @Test
    fun `single return always the same instance`() = runTest {
        // Fixtures
        val role = TechRoleDomainModel.JUNIOR_DEVELOPER
        val workers = listOf(
            TechWorkerDomainModel(
                id = WorkerIdDomainModel("id"),
                name = "name",
                role = role,
                experience = LevelDomainModel(1),
                productivity = LevelDomainModel(1),
                sarcasmLevel = LevelDomainModel(1),
                burnoutRisk = LevelDomainModel(1),
            )
        )

        // Expected
        val expected = workers.size

        // Mock
        everySuspend { listWorkersByRoleUseCase.invoke(any()) } returns workers

        // Test
        val result = sut.invoke(role)

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersByRoleUseCase.invoke(role)
        }
    }

}