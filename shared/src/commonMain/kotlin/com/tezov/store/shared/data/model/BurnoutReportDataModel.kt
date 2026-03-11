package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.BurnoutReportDomainModel

data class BurnoutReportDataModel(
    val severity: Int,
    val reason: String
)

fun BurnoutReportDataModel.toBurnoutReportDomainModel(): BurnoutReportDomainModel =
    BurnoutReportDomainModel(severity = severity, reason = reason)