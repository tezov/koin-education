package com.tezov.store.shared.domain.models

data class WorkResultDomainModel(
    val deliveredFeatures: Int,
    val bugsIntroduced: Int,
    val techDebtCreated: Int
)