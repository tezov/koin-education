package com.tezov.store.shared.presentation.pages.techRoleDetailPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.usecases.ListWorkersByRoleUseCase
import com.tezov.store.shared.presentation.models.WorkerDetailUiModel
import com.tezov.store.shared.presentation.models.toDetailUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TechRoleDetailsViewModel(
    role: String,
    private val listWorkersByRoleUseCase: ListWorkersByRoleUseCase
) : ViewModel() {

    private val _workers = MutableStateFlow<List<WorkerDetailUiModel>>(emptyList())
    val workers: StateFlow<List<WorkerDetailUiModel>> = _workers

    init {
        val roleDomain = TechRoleDomainModel.valueOf(role)
        viewModelScope.launch {
            _workers.value = listWorkersByRoleUseCase(roleDomain)
                .map { it.toDetailUiModel() }
        }
    }
}