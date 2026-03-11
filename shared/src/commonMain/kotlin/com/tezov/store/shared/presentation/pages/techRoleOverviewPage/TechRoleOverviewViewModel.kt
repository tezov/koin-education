package com.tezov.store.shared.presentation.pages.techRoleOverviewPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.usecases.AverageBurnoutRiskByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageExperienceByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageProductivityByRoleUseCase
import com.tezov.store.shared.domain.usecases.AverageSarcasmByRoleUseCase
import com.tezov.store.shared.domain.usecases.CountWorkersByRoleUseCase
import com.tezov.store.shared.presentation.models.TechRoleCardUiModel
import com.tezov.store.shared.presentation.models.toWorkerCardUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TechRoleOverviewViewModel(
    private val countWorkersByRoleUseCase: CountWorkersByRoleUseCase,
    private val avgExperienceByRoleUseCase: AverageExperienceByRoleUseCase,
    private val avgProductivityByRoleUseCase: AverageProductivityByRoleUseCase,
    private val avgSarcasmByRoleUseCase: AverageSarcasmByRoleUseCase,
    private val avgBurnoutRiskByRoleUseCase: AverageBurnoutRiskByRoleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<List<TechRoleCardUiModel>>(emptyList())
    val state: StateFlow<List<TechRoleCardUiModel>> = _state

    init {
        loadRolesData()
    }

    private fun loadRolesData() {
        viewModelScope.launch {
            val rolesData = TechRoleDomainModel.entries.map { role ->
                role.toWorkerCardUiModel(
                    workerCount = countWorkersByRoleUseCase(role),
                    avgExperience = avgExperienceByRoleUseCase(role),
                    avgProductivity = avgProductivityByRoleUseCase(role),
                    avgSarcasm = avgSarcasmByRoleUseCase(role),
                    avgBurnoutRisk = avgBurnoutRiskByRoleUseCase(role)
                )
            }
            _state.value = rolesData
        }
    }
}