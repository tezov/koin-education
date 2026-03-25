package com.tezov.store.shared.data.source

import com.tezov.store.shared.data.model.BurnoutReportDataModel
import com.tezov.store.shared.data.model.TechRoleDataModel
import com.tezov.store.shared.data.model.TechWorkerDataModel
import com.tezov.store.shared.data.model.WorkResultDataModel
import org.koin.core.annotation.Single
import kotlin.random.Random

private val firstNames = listOf(
    "Alice", "Bob", "Charlie", "Dana", "Eve", "Frank", "Grace", "Hank",
    "Ivy", "Jack", "Karen", "Leo", "Mona", "Nate", "Olivia", "Paul",
    "Quinn", "Rita", "Sam", "Tina", "Uma", "Victor", "Wendy", "Xander",
    "Yara", "Zane"
)

private val lastNames = listOf(
    "Smith", "Johnson", "Brown", "Lee", "Taylor", "Anderson", "Thomas",
    "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez",
    "Robinson", "Clark", "Lewis", "Walker", "Hall", "Allen", "Young",
    "King", "Wright", "Scott", "Green"
)

private fun randomName(): String =
    "${firstNames.random()} ${lastNames.random()}"

private fun randomSentence(): String =
    listOf(
        "Overworked and tired",
        "Needs coffee",
        "Burnout imminent",
        "Slack messages ignored",
        "Working late nights",
        "Code review overload",
        "Debugging hell",
        "Meeting fatigue",
        "Push notifications everywhere",
        "Overcommitted sprints"
    ).random()

@Single
class FakeTechWorkerDataSource {

    private val workers = mutableListOf<TechWorkerDataModel>()

    init {
        TechRoleDataModel.entries.forEach { role ->
            repeat(Random.nextInt(2, 6)) { index ->
                val workResult = WorkResultDataModel(
                    deliveredFeatures = Random.nextInt(1, 10),
                    bugsIntroduced = Random.nextInt(0, 5),
                    techDebtCreated = Random.nextInt(0, 3)
                )
                val burnoutReport = BurnoutReportDataModel(
                    severity = Random.nextInt(1, 10),
                    reason = randomSentence()
                )
                workers.add(
                    TechWorkerDataModel(
                        id = "${role.name.lowercase()}-$index",
                        name = randomName(),
                        role = role,
                        experience = Random.nextInt(1, 10),
                        productivity = Random.nextInt(1, 10),
                        sarcasmLevel = Random.nextInt(1, 10),
                        burnoutRisk = Random.nextInt(1, 10),
                        burnoutReport = burnoutReport,
                        workResult = workResult
                    )
                )
            }
        }
    }

    fun getAllWorkers(): List<TechWorkerDataModel> = workers.toList()
    fun findWorkersByRole(role: TechRoleDataModel): List<TechWorkerDataModel> =
        workers.filter { it.role == role }

    fun getWorkerById(id: String): TechWorkerDataModel? = workers.find { it.id == id }
    fun releaseWorkers(ids: List<String>) {
        workers.removeAll { it.id in ids }
    }
}