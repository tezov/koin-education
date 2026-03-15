package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared._system.fixtures.GenerateWorkers
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
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
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ListWorkersByRoleUseCaseTest {
    private lateinit var repository: TechIndustryRepositoryProtocol

    private lateinit var sut: ListWorkersByRoleUseCase

    @BeforeTest
    fun setUp() {
        repository = mock()
        sut = ListWorkersByRoleUseCase(repository)
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(repository)
    }

    @Test
    fun `empty workers returns empty list`() = runTest {
        // Expected
        val expected = emptyList<TechWorkerDomainModel>()

        // Fixtures
        val workers = emptyList<TechWorkerDomainModel>()

        // Mock
        everySuspend { repository.getAllWorkers() } returns workers

        // Test
        val result = sut.invoke()

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            repository.getAllWorkers()
        }
    }

    @Test
    fun `workers without role filter returns all workers`() = runTest {

        // Expected
        val expected = Random.nextInt(5, 25)

        // Fixtures
        val workers = GenerateWorkers.createList(expected)

        // Mock
        everySuspend { repository.getAllWorkers() } returns workers

        // Test
        val result = sut.invoke()

        // Assert
        assertEquals(expected, result.size)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            repository.getAllWorkers()
        }
    }

    @Test
    fun `workers with single role filter returns only matching workers`() = runTest {
        for (role in TechRoleDomainModel.entries) {
            val randomCount = Random.nextInt(TechRoleDomainModel.entries.size, 30)

            // Fixtures
            val workers = GenerateWorkers.createList(randomCount, TechRoleDomainModel.entries)

            // Expected
            val expected = workers.count { it.role == role }

            // Mock
            everySuspend { repository.getAllWorkers() } returns workers

            // Test
            val result = sut.invoke(role)

            // Assert
            assertNotEquals(expected, workers.size)
            assertEquals(expected, result.size)
            assertTrue(result.all { it.role == role })

            // Verify
            verifySuspend(VerifyMode.exhaustiveOrder) {
                repository.getAllWorkers()
            }
        }
    }

}