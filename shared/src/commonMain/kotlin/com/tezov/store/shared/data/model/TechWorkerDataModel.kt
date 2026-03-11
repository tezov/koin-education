package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel

data class TechWorkerDataModel(
    val id: String,
    val name: String,
    val role: TechRoleDataModel,
    val experience: Int,
    val productivity: Int,
    val sarcasmLevel: Int,
    val burnoutRisk: Int,
    val burnoutReport: BurnoutReportDataModel,
    val workResult: WorkResultDataModel
)

fun TechWorkerDataModel.toTechWorkerDomainModel(): TechWorkerDomainModel =
    TechWorkerDomainModel(
        id = WorkerIdDomainModel(id),
        name = name,
        role = role.toTechRoleDomainModel(),
        experience = LevelDomainModel(experience),
        productivity = LevelDomainModel(productivity),
        sarcasmLevel = LevelDomainModel(sarcasmLevel),
        burnoutRisk = LevelDomainModel(burnoutRisk)
    )

