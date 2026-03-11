package com.tezov.store.shared.domain.protocol

import com.tezov.store.shared.domain.models.BurnoutReportDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkResultDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel

interface TechIndustryRepositoryProtocol {

    suspend fun getAllWorkers(): List<TechWorkerDomainModel>

    suspend fun findWorkersByRole(role: TechRoleDomainModel): List<TechWorkerDomainModel>

    suspend fun getBurnOutReportWorker(workerId: WorkerIdDomainModel): BurnoutReportDomainModel

    suspend fun consumeWorker(workerId: WorkerIdDomainModel): WorkResultDomainModel

    suspend fun releaseWorkers(workerIds: List<WorkerIdDomainModel>)

}