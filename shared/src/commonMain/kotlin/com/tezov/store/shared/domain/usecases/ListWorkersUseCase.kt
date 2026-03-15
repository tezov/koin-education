package com.tezov.store.shared.domain.usecases

import com.tezov.store.shared.annotation.OpenForTest
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import org.koin.core.annotation.Factory

@OpenForTest
@Factory
class ListWorkersByRoleUseCase(
    private val repository: TechIndustryRepositoryProtocol
) {
    suspend operator fun invoke(role: TechRoleDomainModel? = null): List<TechWorkerDomainModel> {
        return role?.let {
            repository.getAllWorkers().filter { it.role == role }
        } ?: repository.getAllWorkers()
    }
}