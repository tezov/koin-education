package com.tezov.store.shared.presentation.models

import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel

data class WorkerCardUiModel(
    val id: WorkerIdDomainModel,
    val role: TechRoleDomainModel,
    val displayName: String
)


fun TechWorkerDomainModel.toWorkerCardUiModel(): WorkerCardUiModel {
    return WorkerCardUiModel(
        id = this.id,
        role = this.role,
        displayName = this.name
    )
}