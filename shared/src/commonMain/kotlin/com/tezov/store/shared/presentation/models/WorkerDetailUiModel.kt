package com.tezov.store.shared.presentation.models

import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel

data class WorkerDetailUiModel(
    val id: WorkerIdDomainModel,
    val role: TechRoleDomainModel,
    val displayName: String,
    val experience: Int,
    val productivity: Int,
    val sarcasm: Int,
    val burnoutRisk: Int
)

fun TechWorkerDomainModel.toDetailUiModel(): WorkerDetailUiModel {
    return WorkerDetailUiModel(
        id = this.id,
        role = this.role,
        displayName = this.name,
        experience = this.experience.value,
        productivity = this.productivity.value,
        sarcasm = this.sarcasmLevel.value,
        burnoutRisk = this.burnoutRisk.value
    )
}