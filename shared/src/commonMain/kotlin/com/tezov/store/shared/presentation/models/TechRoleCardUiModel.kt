package com.tezov.store.shared.presentation.models

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel

data class TechRoleCardUiModel(
    val role: TechRoleDomainModel,
    val workerCount: Int,
    val avgExperience: Int?,
    val avgProductivity: Int?,
    val avgSarcasm: Int?,
    val avgBurnoutRisk: Int?
)

fun TechRoleDomainModel.toWorkerCardUiModel(
    workerCount: Int,
    avgExperience: LevelDomainModel?,
    avgProductivity: LevelDomainModel?,
    avgSarcasm: LevelDomainModel?,
    avgBurnoutRisk: LevelDomainModel?
): TechRoleCardUiModel {
    return TechRoleCardUiModel(
        role = this,
        workerCount = workerCount,
        avgExperience = avgExperience?.value,
        avgProductivity = avgProductivity?.value,
        avgSarcasm = avgSarcasm?.value,
        avgBurnoutRisk = avgBurnoutRisk?.value
    )
}