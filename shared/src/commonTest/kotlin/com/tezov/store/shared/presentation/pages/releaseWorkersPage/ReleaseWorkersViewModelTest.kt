package com.tezov.store.shared.presentation.pages.releaseWorkersPage

import com.tezov.store.shared._system.fixtures.GenerateWorkers
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import com.tezov.store.shared.domain.usecases.ListWorkersByRoleUseCase
import com.tezov.store.shared.domain.usecases.ReleaseWorkersUseCase
import com.tezov.store.shared.presentation.models.toWorkerCardUiModel
import com.tezov.store.shared.presentation.pages.releaseWorkersPage.ReleaseWorkersViewModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ReleaseWorkersViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var listWorkersUseCase: ListWorkersByRoleUseCase
    private lateinit var releaseWorkersUseCase: ReleaseWorkersUseCase

    private lateinit var sut: ReleaseWorkersViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        listWorkersUseCase = mock()
        releaseWorkersUseCase = mock()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        verifyNoMoreCalls(listWorkersUseCase, releaseWorkersUseCase)
    }

    private fun createSut() = ReleaseWorkersViewModel(listWorkersUseCase, releaseWorkersUseCase)

    @Test
    fun `init loads workers into state`() = runTest {
        // Expected
        val workers = GenerateWorkers.createList(5)
        val expected = workers.map { it.toWorkerCardUiModel() }

        // Mock
        everySuspend { listWorkersUseCase.invoke(any()) } returns workers

        // Test
        sut = createSut()
        advanceUntilIdle()

        val result = sut.allWorkers.value

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersUseCase.invoke(null)
        }
    }

    @Test
    fun `toggleWorkerSelection ignores unknown workers`() = runTest {
        // Fixtures
        val workers = GenerateWorkers.createList(1)
        val unknownId = WorkerIdDomainModel("unknown")

        everySuspend { listWorkersUseCase.invoke(any()) } returns workers

        sut = createSut()
        advanceUntilIdle()

        // Test
        sut.toggleWorkerSelection(unknownId)

        // Assert
        assertTrue(sut.selectedWorkers.value.isEmpty())

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersUseCase.invoke(null)
        }
    }

    @Test
    fun `toggleWorkerSelection adds worker when not selected`() = runTest {
        // Fixtures
        val workers = GenerateWorkers.createList(1)
        val workerId = workers.first().id

        everySuspend { listWorkersUseCase.invoke(any()) } returns workers

        sut = createSut()
        advanceUntilIdle()

        // Test
        sut.toggleWorkerSelection(workerId)

        // Assert
        assertTrue(workerId in sut.selectedWorkers.value)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersUseCase.invoke(null)
        }
    }

    @Test
    fun `toggleWorkerSelection removes worker when already selected`() = runTest {
        // Fixtures
        val workerId = WorkerIdDomainModel("id-1")

        everySuspend { listWorkersUseCase.invoke(any()) } returns emptyList()
        sut = createSut()
        advanceUntilIdle()

        sut.toggleWorkerSelection(workerId)

        // Test
        sut.toggleWorkerSelection(workerId)

        // Assert
        assertTrue(workerId !in sut.selectedWorkers.value)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersUseCase.invoke(null)
        }
    }

    @Test
    fun `sendReleaseRequest calls releaseWorkers use case and clears workers selection`() =
        runTest {
            // Fixtures
            val workers = GenerateWorkers.Companion.createList(2)
            val workerIds = workers.map { it.id }

            everySuspend { listWorkersUseCase.invoke(any()) } returns workers
            everySuspend { releaseWorkersUseCase.invoke(any()) } returns Unit

            sut = createSut()
            advanceUntilIdle()

            workerIds.forEach { sut.toggleWorkerSelection(it) }

            // Test
            sut.sendReleaseRequest()
            advanceUntilIdle()

            // Assert
            assertTrue(sut.selectedWorkers.value.isEmpty())

            // Verify
            verifySuspend(VerifyMode.exhaustiveOrder) {
                listWorkersUseCase.invoke(null)
                releaseWorkersUseCase.invoke(workerIds)
            }
        }

    @Test
    fun `cancelRelease clears selection`() = runTest {
        // Fixtures
        val workerId = WorkerIdDomainModel("id-1")

        everySuspend { listWorkersUseCase.invoke(any()) } returns emptyList()
        sut = createSut()
        advanceUntilIdle()

        sut.toggleWorkerSelection(workerId)

        // Test
        sut.cancelRelease()

        // Assert
        assertTrue(sut.selectedWorkers.value.isEmpty())

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersUseCase.invoke(null)
        }
    }
}