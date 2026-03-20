package com.tezov.store.shared.presentation.pages.releaseWorkersPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import com.tezov.store.shared.domain.usecases.ListWorkersByRoleUseCase
import com.tezov.store.shared.domain.usecases.ReleaseWorkersUseCase
import com.tezov.store.shared.presentation.models.WorkerCardUiModel
import com.tezov.store.shared.presentation.models.toWorkerCardUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ReleaseWorkersViewModel(
    private val listWorkersUseCase: ListWorkersByRoleUseCase,
    private val releaseWorkersUseCase: ReleaseWorkersUseCase
) : ViewModel() {

    private val _allWorkers = MutableStateFlow<List<WorkerCardUiModel>>(emptyList())
    val allWorkers: StateFlow<List<WorkerCardUiModel>> = _allWorkers

    private val _selectedWorkers = MutableStateFlow<Set<WorkerIdDomainModel>>(emptySet())
    val selectedWorkers: StateFlow<Set<WorkerIdDomainModel>> = _selectedWorkers

    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        viewModelScope.launch {
            val workers = listWorkersUseCase().map { it.toWorkerCardUiModel() }
            _allWorkers.value = workers
        }
    }

    fun toggleWorkerSelection(workerId: WorkerIdDomainModel) {
        val knownIds = _allWorkers.value.map { it.id }.toSet()
        if (workerId !in knownIds) return
        _selectedWorkers.value = _selectedWorkers.value.toMutableSet().apply {
            if (contains(workerId)) remove(workerId) else add(workerId)
        }
    }

    fun sendReleaseRequest() {
        viewModelScope.launch {
            releaseWorkersUseCase(_selectedWorkers.value.toList())
            _selectedWorkers.value = emptySet()
        }
    }

    fun cancelRelease() {
        _selectedWorkers.value = emptySet()
    }
}