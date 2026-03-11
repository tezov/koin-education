package com.tezov.store.shared.data

import com.tezov.store.shared.data.model.toBurnoutReportDomainModel
import com.tezov.store.shared.data.model.toTechRoleDataModel
import com.tezov.store.shared.data.model.toTechWorkerDomainModel
import com.tezov.store.shared.data.model.toWorkResultDomainModel
import com.tezov.store.shared.data.source.FakeTechWorkerDataSource
import com.tezov.store.shared.domain.models.BurnoutReportDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkResultDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import com.tezov.store.shared.domain.protocol.TechIndustryRepositoryProtocol
import org.koin.core.annotation.Factory

@Factory
class TechIndustryRepository(
    private val source: FakeTechWorkerDataSource
) : TechIndustryRepositoryProtocol {

    override suspend fun getAllWorkers(): List<TechWorkerDomainModel> =
        source.getAllWorkers().map { it.toTechWorkerDomainModel() }

    override suspend fun findWorkersByRole(role: TechRoleDomainModel): List<TechWorkerDomainModel> =
        source.findWorkersByRole(role.toTechRoleDataModel()).map { it.toTechWorkerDomainModel() }

    override suspend fun getBurnOutReportWorker(workerId: WorkerIdDomainModel): BurnoutReportDomainModel {
        val worker = source.getWorkerById(workerId.value)
            ?: throw IllegalArgumentException("Worker not found")
        return worker.burnoutReport.toBurnoutReportDomainModel()
    }

    override suspend fun consumeWorker(workerId: WorkerIdDomainModel): WorkResultDomainModel {
        val worker = source.getWorkerById(workerId.value)
            ?: throw IllegalArgumentException("Worker not found")
        return worker.workResult.toWorkResultDomainModel()
    }

    override suspend fun releaseWorkers(workerIds: List<WorkerIdDomainModel>) {
        source.releaseWorkers(workerIds.map { it.value })
    }
}