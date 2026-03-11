package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.domain.models.TechRoleDomainModel
import org.koin.core.annotation.Factory

@Factory
class CountWorkersByRoleUseCase(
    private val listWorkersByRole: ListWorkersByRoleUseCase
) {
    suspend operator fun invoke(role: TechRoleDomainModel): Int {
        return listWorkersByRole(role).size
    }
}