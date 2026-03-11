package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import org.koin.core.annotation.Factory

@Factory
class AverageProductivityByRoleUseCase(
    private val repository: TechIndustryRepositoryProtocol
) {
    suspend operator fun invoke(role: TechRoleDomainModel): LevelDomainModel? {
        val workersInRole = repository.getAllWorkers().filter { it.role == role }
        if (workersInRole.isEmpty()) return null
        val avg = workersInRole.map { it.productivity.value }.average().toInt()
        return LevelDomainModel(avg)
    }
}