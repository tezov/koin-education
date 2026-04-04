package com.tezov.store.shared.dependencies.koinAnnotation

import org.koin.core.Koin
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Qualifier
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.plugin.module.dsl.koinApplication
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Module
@ComponentScan("com.tezov.store.shared.dependencies.koinAnnotation")
@Configuration("test")
object SharedModuleTest

@Module
@ComponentScan("com.tezov.store.shared.dependencies.koinAnnotation")
@Configuration("test")
object SharedModuleTest2 {
    // Module used like Dagger/Hilt that provide stuff

    @Single
    class Context

    // We don't own the class or wants extra logic, so we use the provider function way
    class AppDatabase(context: Context)

    @Single // single / factory work also on function, context is injected since koin know about it
    fun providesDatabase(context: Context): AppDatabase = AppDatabase(context)

}

@KoinApplication(
    modules = [SharedModuleTest::class, SharedModuleTest2::class],
    configurations = ["test"] // could have different configuration, or use default. For the demo, I used a test configuration
)
object SharedApplicationTest

var koinIsolated: Koin? = null

class KoinAnnotationTest {

    @AfterTest
    fun tearDown() {
        koinIsolated?.close()
        koinIsolated = null
    }

//************************************ Single

    @Single
    class SingleClass {
        @OptIn(ExperimentalUuidApi::class)
        val id: String = Uuid.generateV4().toHexString()
    }

    @Test
    fun `single return always the same instance`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<SingleClass>()

        assertIs<SingleClass>(result)
        assertSame(result, koin.get<SingleClass>())
        assertEquals(result.id, koin.get<SingleClass>().id)
    }

//************************************ Factory

    @Factory
    class FactoryClass {
        @OptIn(ExperimentalUuidApi::class)
        val id: String = Uuid.generateV4().toHexString()
    }

    @Test
    fun `factory return always a new instance`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<FactoryClass>()

        assertIs<FactoryClass>(result)
        assertNotSame(result, koin.get<FactoryClass>())
        assertNotEquals(result.id, koin.get<FactoryClass>().id)
    }

//************************************ Named String

    interface Protocol

    @Single
    @Named("protocolA")
    class ProtocolImplementationA : Protocol

    @Factory
    @Named("protocolB")
    class ProtocolImplementationB : Protocol

    @Test
    fun `named allow you to select a specific instance`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val resultA = koin.get<Protocol>(named("protocolA"))

        assertIs<ProtocolImplementationA>(resultA)
        assertSame(resultA, koin.get<Protocol>(named("protocolA")))

        val resultB = koin.get<Protocol>(named("protocolB"))

        assertIs<ProtocolImplementationB>(resultB)
        assertNotSame(resultB, koin.get<Protocol>(named("protocolB")))
    }

    //************************************ Named Annotation
    @Named
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ProtocolE

    @Single
    @ProtocolE
    class ProtocolImplementationE : Protocol

    @Named
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ProtocolF

    @Factory
    @ProtocolF
    class ProtocolImplementationF : Protocol

//    @Test
//    fun `named annotation allow you to select a specific instance`() {
//        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }
//
//        // This doesn't work, fail to resolved, but according to the doc, it is supposed to work
//        val resultE = koin.get<Protocol>(named<ProtocolE>())
//
//        assertIs<ProtocolImplementationE>(resultE)
//        assertSame(resultE, koin.get<Protocol>(named<ProtocolE>()))
//
//        val resultF = koin.get<Protocol>(named<ProtocolF>())
//
//        assertIs<ProtocolImplementationF>(resultF)
//        assertNotSame(resultF, koin.get<Protocol>(named<ProtocolF>()))
//    }

//************************************ Qualifier simple manual get

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ProtocolNameSimple

    @Factory
    @ProtocolNameSimple
    class ProtocolImplementationG : Protocol

//    @Test
//    fun `simple qualifier manual get allow you to select a specific instance`() {
//        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }
//
//        // This doesn't work, fail to resoled, documentation doesn't show it, but the code is similar to named(...)
//        val result = koin.get<ProtocolImplementationG>(qualifier<ProtocolNameSimple>())
//
//        assertIs<ProtocolImplementationG>(result)
//        assertNotSame(result, koin.get<ProtocolImplementationG>())
//    }

//************************************ Qualifier simple

    @Factory
    class ReceptorG(
        @param:ProtocolNameSimple val g: Protocol,
    )

    @Test
    fun `simple qualifier allow you to select a specific instance`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<ReceptorG>()

        assertIs<ProtocolImplementationG>(result.g)
        assertNotSame(result.g, koin.get<ReceptorG>().g)

    }

//************************************ Qualifier complex

    enum class ProtocolNameKey { C, D }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ProtocolNameComplex(val key: ProtocolNameKey)

    @Single
    @ProtocolNameComplex(ProtocolNameKey.C)
    class ProtocolImplementationC : Protocol

    @Factory
    @ProtocolNameComplex(ProtocolNameKey.D)
    class ProtocolImplementationD : Protocol

    @Factory
    class ReceptorCD(
        @param:ProtocolNameComplex(ProtocolNameKey.C) val c: Protocol,
        @param:ProtocolNameComplex(ProtocolNameKey.D) val d: Protocol
    )

    @Test
    fun `complex qualifier allow you to select a specific instance`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<ReceptorCD>()

        assertIs<ProtocolImplementationC>(result.c)
        assertSame(result.c, koin.get<ReceptorCD>().c)

        assertIs<ProtocolImplementationD>(result.d)
        assertNotSame(result.d, koin.get<ReceptorCD>().d)
    }

//************************************ Injected Param

    @Single
    class SingleWithParam(
        @InjectedParam val param1: String,
        @InjectedParam val param2: Int,
    )

    @Factory
    class FactoryWithParam(
        @InjectedParam val param1: String,
        @InjectedParam val param2: Int,
    )

    @Test
    fun `injected param allow you to pass parameter to koin resolver`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        // Single *****************************************************

        val resultSingle = koin.get<SingleWithParam>(parameters = {
            parametersOf(
                /* param1 */ "single-param1",
                /* param2 */ 3
            )
        })

        assertIs<SingleWithParam>(resultSingle)
        assertSame(resultSingle, koin.get<SingleWithParam>(parameters = {
            parametersOf(
                /* param1 */ "single-param1-different", //--> will be ignored since SingleWithParam is Singleton
                /* param2 */ 12 //--> ignored too
            )
        }))
        assertEquals(resultSingle.param1, "single-param1")
        assertEquals(resultSingle.param2, 3)

        //Factory *****************************************************

        val resultFactory = koin.get<FactoryWithParam>(parameters = {
            parametersOf(
                /* param1 */ "factory-param1",
                /* param2 */ 8
            )
        })

        assertIs<FactoryWithParam>(resultFactory)
        assertNotSame(resultFactory, koin.get<FactoryWithParam>(parameters = {
            parametersOf(
                /* param1 */ "factory-param1-different",
                /* param2 */ 17
            )
        }))
        assertEquals(resultFactory.param1, "factory-param1")
        assertEquals(resultFactory.param2, 8)

        val resultFactory2 = koin.get<FactoryWithParam>(parameters = {
            parametersOf(
                /* param1 */ "factory-param1-new",
                /* param2 */ 43
            )
        })
        assertEquals(resultFactory2.param1, "factory-param1-new")
        assertEquals(resultFactory2.param2, 43)

    }

//************************************ Get a List of class that respect an Interface

    interface ProtocolList

    @Single
    class ListImplementationA : ProtocolList

    @Factory
    class ListImplementationB : ProtocolList

    @Single
    @Named("listProtocolNamed")
    class ListImplementationC : ProtocolList

    @Factory
    class ReceptorList(val list: List<ProtocolList>)

    @Test
    fun `argument list are resolved with getAll`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<ReceptorList>()

        assertEquals(3, result.list.size)
        assertTrue(result.list.any { it is ListImplementationA })
        assertTrue(result.list.any { it is ListImplementationB })
        assertTrue(result.list.any { it is ListImplementationC })

        assertSame(
            result.list.first { it is ListImplementationA },
            koin.get<ReceptorList>().list.first { it is ListImplementationA }
        )
        assertNotSame(
            result.list.first { it is ListImplementationB },
            koin.get<ReceptorList>().list.first { it is ListImplementationB }
        )
        assertSame(
            result.list.first { it is ListImplementationC },
            koin.get<ReceptorList>().list.first { it is ListImplementationC }
        )

    }


//************************************ Test the Dagger/Hilt style provider

    @Test
    fun `dagger hilt style provider`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<SharedModuleTest2.AppDatabase>()

        assertSame(result, koin.get<SharedModuleTest2.AppDatabase>())

    }

//************************************ Late Declaration
    class LateDeclaration

    @Single
    class SingleWithLateDeclaration(@Provided val lateDeclaration: LateDeclaration)

    @Factory
    class FactoryWithLateDeclaration(@Provided val lateDeclaration: LateDeclaration)

    @Test
    fun `late declaration can be provided later`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val lateDeclaration = LateDeclaration()
        koin.declare(lateDeclaration)

        val resultSingle = koin.get<SingleWithLateDeclaration>()
        assertSame(lateDeclaration, resultSingle.lateDeclaration)

        val resultFactory = koin.get<FactoryWithLateDeclaration>()
        assertSame(lateDeclaration, resultFactory.lateDeclaration)

    }

//************************************ Scope

    @Factory
    class UnscopedClass

    @Scope
    class ScopeA

    @Scope(ScopeA::class)
    @Scoped
    class ScopedSingleClass(val unscopedClass: UnscopedClass)

    @Scope(ScopeA::class)
    @Factory
    class ScopedFactoryClass(val unscopedClass: UnscopedClass)

    @Test
    fun `scope annotation`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val scope = koin.createScope<ScopeA>("scopeA-id-1")

        val resultSingle = scope.get<ScopedSingleClass>()
        assertIs<ScopedSingleClass>(resultSingle)
        assertSame(resultSingle.unscopedClass, scope.get<ScopedSingleClass>().unscopedClass)

        val resultFactory = scope.get<ScopedFactoryClass>()
        assertIs<ScopedFactoryClass>(resultFactory)
        assertNotSame(resultFactory.unscopedClass, scope.get<ScopedFactoryClass>().unscopedClass)

        scope.close()
    }

//************************************ Scope with current scope injection
    @Scope
    class ScopeC

    @Scope(ScopeC::class)
    @Scoped
    class ScopedSingleClassWithCurrentScope(
        @Provided val currentScope: org.koin.core.scope.Scope,
    )

    @Test
    fun `scope annotation with current scope injected`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val scope = koin.createScope<ScopeC>("scopeC-id-1")
        scope.declare(scope) // -> Here we late declare the scope to be able to inject it

        val result = scope.get<ScopedSingleClassWithCurrentScope>()
        assertSame(result.currentScope, scope)

        scope.close()
    }

//************************************ Scope with late declaration
    @Scope
    class ScopeB

    @Scope(ScopeB::class)
    @Scoped
    class ScopedSingleClassWithLateDeclaration(
        @Provided val lateDeclaration: LateDeclaration
    )

    @Test
    fun `scope annotation with late declaration`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val scope = koin.createScope<ScopeB>("scopeB-id-1")
        val lateDeclaration = LateDeclaration().also {
            scope.declare(it)
        }

        val result = scope.get<ScopedSingleClassWithLateDeclaration>()


        assertSame(result.lateDeclaration, lateDeclaration)

        scope.close()
    }

//************************************ Lazy injection in constructor

    @Single
    class LazySingleClass

    @Factory
    class LazyFactoryClass

    @Factory
    class ReceptorLazyConstructor(
        val s: Lazy<LazySingleClass>,
        val f: Lazy<LazyFactoryClass>
    )

    @Test
    fun `lazy injection in constructor`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<ReceptorLazyConstructor>()

        assertFalse(result.s.isInitialized())
        assertFalse(result.f.isInitialized())

        assertSame(result.s.value, koin.get<ReceptorLazyConstructor>().s.value)
        assertNotSame(result.f.value, koin.get<ReceptorLazyConstructor>().f.value)
    }

//************************************ Lazy injection through interface

    interface KoinComponentIsolatedTest : KoinComponent {
        override fun getKoin(): Koin = koinIsolated!!
    }

    @Factory
    class ReceptorLazy : KoinComponentIsolatedTest {
        // for test, I'm forced to use isolated context,
        // but in simple application, just use KoinComponent interface directly

        val s by inject<LazySingleClass>()
        val f by inject<LazyFactoryClass>()
    }

    @Test
    fun `lazy injection to solve cyclic dependencies`() {
        val koin = koinApplication<SharedApplicationTest> { }.koin.also { koinIsolated = it }

        val result = koin.get<ReceptorLazy>()

        assertSame(result.s, koin.get<ReceptorLazy>().s)
        assertNotSame(result.f, koin.get<ReceptorLazy>().f)
    }

}