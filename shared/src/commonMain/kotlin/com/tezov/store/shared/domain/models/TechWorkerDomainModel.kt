package com.tezov.store.shared.domain.models

data class TechWorkerDomainModel(
    val id: WorkerIdDomainModel,
    val name: String,
    val role: TechRoleDomainModel,
    val experience: LevelDomainModel,
    val productivity: LevelDomainModel,
    val sarcasmLevel: LevelDomainModel,
    val burnoutRisk: LevelDomainModel
)