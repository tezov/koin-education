package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.WorkResultDomainModel

data class WorkResultDataModel(
    val deliveredFeatures: Int,
    val bugsIntroduced: Int,
    val techDebtCreated: Int
)

fun WorkResultDataModel.toWorkResultDomainModel(): WorkResultDomainModel =
    WorkResultDomainModel(
        deliveredFeatures = deliveredFeatures,
        bugsIntroduced = bugsIntroduced,
        techDebtCreated = techDebtCreated
    )