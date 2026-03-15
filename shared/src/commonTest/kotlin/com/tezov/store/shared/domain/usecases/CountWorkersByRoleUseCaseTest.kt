package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared._system.fixtures.GenerateWorkers
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
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
    fun `empty workers with role returns 0`() = runTest {
        // Expected
        val expected = 0

        // Fixtures
        val role = TechRoleDomainModel.JUNIOR_DEVELOPER
        val workers = emptyList<TechWorkerDomainModel>()

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

    @Test
    fun `single workers with role returns 1`() = runTest {
        for (role in TechRoleDomainModel.entries) {
            // Expected
            val expected = 1

            // Fixtures
            val workers = GenerateWorkers.createList(expected, role)

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

    @Test
    fun `between 2-15 workers with role returns correct size`() = runTest {
        for (role in TechRoleDomainModel.entries) {
            // Expected
            val expected = Random.nextInt(2, 15)

            // Fixtures
            val workers = GenerateWorkers.createList(expected, role)

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

}