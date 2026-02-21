package com.tezov.koineducation

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class KoinTest {

    data class A(val id: String)

    interface BProtocol {
        val id: String
    }

    data class B(override val id: String) : BProtocol

    class CFixedId : BProtocol {
        override val id: String = "fixed-id"
    }

    class DRandomId : BProtocol {
        override val id: String = Uuid.generateV4().toHexString()
    }

    data class BWrapper(val value: BProtocol)

    @Test
    fun `single return always the same instance`() {
        val koinApplication = koinApplication {
            modules(module {
                single {
                    val singleUUID = Uuid.generateV4().toHexString()
                    println("uuid = $singleUUID")
                    A(singleUUID)
                }
            })
        }

        val first = koinApplication.koin.get<A>()
        val second = koinApplication.koin.get<A>()
        val third = koinApplication.koin.get<A>()

        koinApplication.close()

        assertEquals(first.id, second.id, third.id)
        assertSame(first, second)
        assertSame(second, third)
    }

    @Test
    fun `factory return always a new instance`() {
        val koinApplication = koinApplication {
            modules(module {
                factory {
                    val singleUUID = Uuid.generateV4().toHexString()
                    println("uuid = $singleUUID")
                    A(singleUUID)
                }
            })
        }

        val first = koinApplication.koin.get<A>()
        val second = koinApplication.koin.get<A>()
        val third = koinApplication.koin.get<A>()

        koinApplication.close()

        assertNotEquals(first.id, second.id, third.id)
        assertNotSame(first, second)
        assertNotSame(second, third)
    }

    @Test
    fun `qualifier allow you to select a specific instance`() {
        val koinApplication = koinApplication {
            modules(module {
                single<BProtocol>(named("first-qualifier")) { B("First") }
                single<BProtocol>(named("second-qualifier")) { B("Second") }
            })
        }

        val first = koinApplication.koin.get<BProtocol>(named("first-qualifier"))
        val second = koinApplication.koin.get<BProtocol>(named("second-qualifier"))

        koinApplication.close()

        assertNotEquals(first.id, second.id)
        assertEquals("First", first.id)
        assertEquals("Second", second.id)

    }

    @Test
    fun `scope allow you have multiple isolated context resolution`() {
        val koinApplication = koinApplication {
            modules(module {
                scope(named("scope")) {
                    scoped<A> { A("single(scoped)") }
                }
            })
        }

        val firstScope = koinApplication.koin.createScope("scope1", named("scope"))
        val firstScope_first = firstScope.get<A>()

        val secondScope = koinApplication.koin.createScope("scope2", named("scope"))
        val secondScope_first = secondScope.get<A>()

        firstScope.close()
        secondScope.close()
        koinApplication.close()

        assertNotSame(firstScope_first, secondScope_first)
        assertEquals("single(scoped)", firstScope_first.id)
        assertEquals("single(scoped)", secondScope_first.id)
    }

    @Test
    fun `scope allow you have isolated context resolution`() {
        val koinApplication = koinApplication {
            modules(module {
                scope(named("first-scope")) {
                    scoped<A> { A("single(scoped)-from-first-scope") }
                    factory<BProtocol> { B("factory-from-first-scope") }
                }
                scope(named("second-scope")) {
                    scoped<A> { A("single(scoped)-from-second-scope") }
                    factory<BProtocol> { B("factory-from-second-scope") }
                }
            })
        }

        val firstScope = koinApplication.koin.createScope("scope1", named("first-scope"))
        val firstScope_first = firstScope.get<A>()
        val firstScope_second = firstScope.get<BProtocol>()

        val secondScope = koinApplication.koin.createScope("scope2", named("second-scope"))
        val secondScope_first = secondScope.get<A>()
        val secondScope_second = secondScope.get<BProtocol>()

        firstScope.close()
        secondScope.close()
        koinApplication.close()

        assertNotSame(firstScope_first, secondScope_first)

        assertEquals("single(scoped)-from-first-scope", firstScope_first.id)
        assertEquals("factory-from-first-scope", firstScope_second.id)

        assertEquals("single(scoped)-from-second-scope", secondScope_first.id)
        assertEquals("factory-from-second-scope", secondScope_second.id)
    }

    @Test
    fun `scope can be linked to allow contextual resolution`() {
        val koinApplication = koinApplication {
            modules(module {
                scope(named("first-scope")) {
                    factory<BProtocol> { CFixedId() }
                }
                scope(named("second-scope")) {
                    factory<BProtocol> { DRandomId() }
                }

                scope(named("main-scope")) {
                    factory { BWrapper(value = get<BProtocol>()) }
                }
            })
        }

        val firstScope = koinApplication.koin.createScope("scope1", named("first-scope"))
        val mainScope = koinApplication.koin.createScope("main", named("main-scope"))
        mainScope.linkTo(firstScope)

        val resolvedWithFirstScope = mainScope.get<BWrapper>()
        println("uuid is ${resolvedWithFirstScope.value.id}")
        assertEquals("fixed-id", resolvedWithFirstScope.value.id)

        mainScope.unlink(firstScope)
        firstScope.close()

        val secondScope = koinApplication.koin.createScope("scope2", named("second-scope"))
        mainScope.linkTo(secondScope)

        val resolvedWithSecondScope = mainScope.get<BWrapper>()
        println("uuid is ${resolvedWithSecondScope.value.id}")
        assertNotEquals("fixed-id", resolvedWithSecondScope.value.id)

        secondScope.close()
        mainScope.close()
        koinApplication.close()
    }

}