package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.annotation.OpenForTest
import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import org.koin.core.annotation.Factory

@OpenForTest
@Factory
class AverageBurnoutRiskByRoleUseCase(
    private val listWorkersByRoleUseCase: ListWorkersByRoleUseCase,
) {
    suspend operator fun invoke(role: TechRoleDomainModel): LevelDomainModel? {
        val workersInRole = listWorkersByRoleUseCase(role)
        if (workersInRole.isEmpty()) return null
        val avg = workersInRole.map { it.burnoutRisk.value }.average().toInt()
        return LevelDomainModel(avg)
    }
}