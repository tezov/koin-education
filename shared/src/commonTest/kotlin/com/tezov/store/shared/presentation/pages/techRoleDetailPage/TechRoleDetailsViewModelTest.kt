package com.tezov.store.shared.presentation.pages.techRoleDetailPage

import com.tezov.store.shared._system.fixtures.GenerateWorkers
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.usecases.ListWorkersByRoleUseCase
import com.tezov.store.shared.presentation.models.WorkerDetailUiModel
import com.tezov.store.shared.presentation.models.toDetailUiModel
import com.tezov.store.shared.presentation.pages.releaseWorkersPage.ReleaseWorkersViewModel
import com.tezov.store.shared.presentation.pages.techRoleDetailPage.TechRoleDetailsViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class TechRoleDetailsViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var listWorkersByRoleUseCase: ListWorkersByRoleUseCase

    private lateinit var sut: TechRoleDetailsViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        listWorkersByRoleUseCase = mock()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        verifyNoMoreCalls(listWorkersByRoleUseCase)
    }

    private fun createSut(role: String) = TechRoleDetailsViewModel(role, listWorkersByRoleUseCase)

    @Test
    fun `init with empty result returns empty list`() = runTest {
        // Fixtures
        val role = TechRoleDomainModel.entries.random()
        val workers = emptyList<TechWorkerDomainModel>()
        val expected = emptyList<WorkerDetailUiModel>()

        // Mock
        everySuspend { listWorkersByRoleUseCase.invoke(any()) } returns workers

        // Test
        sut = createSut(role.name)
        advanceUntilIdle()

        val result = sut.workers.value

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersByRoleUseCase.invoke(role)
        }
    }

    @Test
    fun `init loads workers for given role`() = runTest {
        // Fixtures
        val role = TechRoleDomainModel.entries.random()
        val workers = GenerateWorkers.createList(5, role)
        val expected = workers.map { it.toDetailUiModel() }

        // Mock
        everySuspend { listWorkersByRoleUseCase.invoke(any()) } returns workers

        // Test
        sut = createSut(role.name)
        advanceUntilIdle()

        val result = sut.workers.value

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            listWorkersByRoleUseCase.invoke(role)
        }
    }
}