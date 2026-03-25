package com.tezov.store.shared._system.fixtures

import com.tezov.store.shared.domain.models.LevelDomainModel
import com.tezov.store.shared.domain.models.TechRoleDomainModel
import com.tezov.store.shared.domain.models.TechWorkerDomainModel
import com.tezov.store.shared.domain.models.WorkerIdDomainModel
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GenerateWorkers {

    companion object {

        fun createList(
            count: Int,
            role: TechRoleDomainModel
        ) = createList(
            count = count,
            roles = listOf(role)
        )

        fun createList(
            count: Int,
            roles: List<TechRoleDomainModel> = TechRoleDomainModel.entries.shuffled().take(count)
        ): List<TechWorkerDomainModel> {
            if (count == 0) {
                return emptyList()
            }
            require(count >= roles.size) { "count must be greater than roles size" }
            return buildList {
                var index = 0
                roles.forEach { role ->
                    add(createWorker(index = index, role = role))
                    index++
                }
                repeat(count - roles.size) {
                    add(createWorker(index = index, role = roles.random()))
                    index++
                }
            }.shuffled(Random)
        }

        private fun createWorker(
            index: Int,
            role: TechRoleDomainModel
        ) = TechWorkerDomainModel(
            id = WorkerIdDomainModel("id-$index"),
            name = "name-$index",
            role = role,
            experience = LevelDomainModel(Random.nextInt(1, 10)),
            productivity = LevelDomainModel(Random.nextInt(1, 10)),
            sarcasmLevel = LevelDomainModel(Random.nextInt(1, 10)),
            burnoutRisk = LevelDomainModel(Random.nextInt(1, 10)),
        )
    }

    @Test
    fun `createList with count 0 always return an empty list`() = runTest {
        val expected = 0

        val result = createList(expected)

        assertEquals(expected, result.size)
    }

    @Test
    fun `createList always return the requested amount`() = runTest {
        val expected = listOf(
            12, 4, 29, 7, 18, 0, 25, 9, 3, 21,
            14, 1, 30, 6, 27, 10, 16, 2, 24, 13,
            8, 22, 5, 19, 11, 28, 17, 26, 15, 20
        )
        expected.forEach {
            val result = createList(it, TechRoleDomainModel.entries.random())
            assertEquals(it, result.size)
        }
    }

    @Test
    fun `createList single role generates only that role`() {
        val expected = TechRoleDomainModel.entries.random()

        val result = createList(20, expected)

        assertTrue(result.all { it.role == expected })
    }

    @Test
    fun `createList list ensures at least one of each role`() {
        val expected = TechRoleDomainModel.entries

        val result = createList(expected.size * 3, expected)

        expected.forEach { role ->
            assertTrue(result.any { it.role == role })
        }
    }

    @Test
    fun `createList respects provided roles only`() {
        val roles = TechRoleDomainModel.entries.take(2)

        val result = createList(20, roles)

        assertTrue(result.all { it.role in roles })
    }

    @Test
    fun `worker ids are unique`() {
        val result = createList(50)

        assertEquals(result.size, result.map { it.id }.toSet().size)
    }

    @Test
    fun `throws when count smaller than roles`() {
        val roles = TechRoleDomainModel.entries

        assertFailsWith<IllegalArgumentException> {
            createList(roles.size - 1, roles)
        }
    }

}