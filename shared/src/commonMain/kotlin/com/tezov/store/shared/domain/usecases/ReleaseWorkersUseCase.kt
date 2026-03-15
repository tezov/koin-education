package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.annotation.OpenForTest
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import org.koin.core.annotation.Factory

@OpenForTest
@Factory
class ReleaseWorkersUseCase(
    private val repository: TechIndustryRepositoryProtocol
) {
    suspend operator fun invoke(workerIds: List<WorkerIdDomainModel>) {
        repository.releaseWorkers(workerIds)
    }
}