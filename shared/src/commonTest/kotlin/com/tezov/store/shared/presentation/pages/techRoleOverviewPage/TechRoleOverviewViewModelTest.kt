package com.tezov.store.shared.presentation.pages.techRoleOverviewPage

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.usecases.AverageBurnoutRiskByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageExperienceByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageProductivityByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageSarcasmByRoleUseCase
import com.tezov.store.shared.domain.usecases.CountWorkersByRoleUseCase
import com.tezov.store.shared.presentation.models.toWorkerCardUiModel
import com.tezov.store.shared.presentation.pages.techRoleOverviewPage.TechRoleOverviewViewModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
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
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TechRoleOverviewViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var countWorkersByRoleUseCase: CountWorkersByRoleUseCase
    private lateinit var avgExperienceByRoleUseCase: AverageExperienceByRoleUseCase
    private lateinit var avgProductivityByRoleUseCase: AverageProductivityByRoleUseCase
    private lateinit var avgSarcasmByRoleUseCase: AverageSarcasmByRoleUseCase
    private lateinit var avgBurnoutRiskByRoleUseCase: AverageBurnoutRiskByRoleUseCase

    private lateinit var sut: TechRoleOverviewViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        countWorkersByRoleUseCase = mock()
        avgExperienceByRoleUseCase = mock()
        avgProductivityByRoleUseCase = mock()
        avgSarcasmByRoleUseCase = mock()
        avgBurnoutRiskByRoleUseCase = mock()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        verifyNoMoreCalls(
            countWorkersByRoleUseCase,
            avgExperienceByRoleUseCase,
            avgProductivityByRoleUseCase,
            avgSarcasmByRoleUseCase,
            avgBurnoutRiskByRoleUseCase
        )
    }

    private fun createSut() = TechRoleOverviewViewModel(
        countWorkersByRoleUseCase,
        avgExperienceByRoleUseCase,
        avgProductivityByRoleUseCase,
        avgSarcasmByRoleUseCase,
        avgBurnoutRiskByRoleUseCase
    )

    @Test
    fun `init with null averages returns null values in ui`() = runTest {
        // Fixtures
        val roles = TechRoleDomainModel.entries

        val counts = roles.associateWith { Random.nextInt(0, 50) }

        val expected = roles.map { role ->
            role.toWorkerCardUiModel(
                workerCount = counts.getValue(role),
                avgExperience = null,
                avgProductivity = null,
                avgSarcasm = null,
                avgBurnoutRisk = null
            )
        }

        // Mock
        roles.forEach { role ->
            everySuspend { countWorkersByRoleUseCase.invoke(role) } returns counts.getValue(role)
            everySuspend { avgExperienceByRoleUseCase.invoke(role) } returns null
            everySuspend { avgProductivityByRoleUseCase.invoke(role) } returns null
            everySuspend { avgSarcasmByRoleUseCase.invoke(role) } returns null
            everySuspend { avgBurnoutRiskByRoleUseCase.invoke(role) } returns null
        }

        // Test
        sut = createSut()
        advanceUntilIdle()

        val result = sut.state.value

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            roles.forEach { role ->
                countWorkersByRoleUseCase.invoke(role)
                avgExperienceByRoleUseCase.invoke(role)
                avgProductivityByRoleUseCase.invoke(role)
                avgSarcasmByRoleUseCase.invoke(role)
                avgBurnoutRiskByRoleUseCase.invoke(role)
            }
        }
    }

    @Test
    fun `init loads roles overview data`() = runTest {
        // Fixtures
        val roles = TechRoleDomainModel.entries

        var seq = 0

        val counts = roles.associateWith { seq++ }
        val exp = roles.associateWith { LevelDomainModel((seq++ % 10) + 1) }
        val prod = roles.associateWith { LevelDomainModel((seq++ % 10) + 1) }
        val sarcasm = roles.associateWith { LevelDomainModel((seq++ % 10) + 1) }
        val burnout = roles.associateWith { LevelDomainModel((seq++ % 10) + 1) }

        // Expected
        val expected = roles.map { role ->
            role.toWorkerCardUiModel(
                workerCount = counts.getValue(role),
                avgExperience = exp.getValue(role),
                avgProductivity = prod.getValue(role),
                avgSarcasm = sarcasm.getValue(role),
                avgBurnoutRisk = burnout.getValue(role)
            )
        }

        // Mock
        roles.forEach { role ->
            everySuspend { countWorkersByRoleUseCase.invoke(role) } returns counts.getValue(role)
            everySuspend { avgExperienceByRoleUseCase.invoke(role) } returns exp.getValue(role)
            everySuspend { avgProductivityByRoleUseCase.invoke(role) } returns prod.getValue(role)
            everySuspend { avgSarcasmByRoleUseCase.invoke(role) } returns sarcasm.getValue(role)
            everySuspend { avgBurnoutRiskByRoleUseCase.invoke(role) } returns burnout.getValue(role)
        }

        // Test
        sut = createSut()
        advanceUntilIdle()

        val result = sut.state.value

        // Assert
        assertEquals(expected, result)

        // Verify
        verifySuspend(VerifyMode.exhaustiveOrder) {
            roles.forEach { role ->
                countWorkersByRoleUseCase.invoke(role)
                avgExperienceByRoleUseCase.invoke(role)
                avgProductivityByRoleUseCase.invoke(role)
                avgSarcasmByRoleUseCase.invoke(role)
                avgBurnoutRiskByRoleUseCase.invoke(role)
            }
        }
    }
}