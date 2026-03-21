package com.tezov.store.shared

import com.tezov.store.shared.annotation.OpenForTest
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.MockMode.autofill
import dev.mokkery.MockMode.original
import dev.mokkery.MokkeryRuntimeException
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.answering.returnsArgAt
import dev.mokkery.answering.returnsBy
import dev.mokkery.answering.sequentially
import dev.mokkery.answering.throws
import dev.mokkery.answering.throwsBy
import dev.mokkery.coroutines.answering.Awaitable.Companion.delayed
import dev.mokkery.coroutines.answering.awaits
import dev.mokkery.debug.printMokkeryDebug
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.capture.Capture
import dev.mokkery.matcher.capture.capture
import dev.mokkery.matcher.capture.get
import dev.mokkery.matcher.matches
import dev.mokkery.mock
import dev.mokkery.mockMany
import dev.mokkery.resetCalls
import dev.mokkery.spy
import dev.mokkery.t1
import dev.mokkery.t2
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atLeast
import dev.mokkery.verify.VerifyMode.Companion.atMost
import dev.mokkery.verify.VerifyMode.Companion.exhaustive
import dev.mokkery.verify.VerifyMode.Companion.inRange
import dev.mokkery.verify.VerifyMode.Companion.order
import dev.mokkery.verifyNoMoreCalls
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MokkeryTest {

    @OpenForTest
    class A {
        fun foo(input: String, another: String = ""): String = input

        fun flowFoo(input: String): Flow<String> = flow { emit(input) }

        suspend fun suspendedFoo(input: String): String {
            delay(50)
            return input
        }

        fun returnUnit() {}
    }

    interface BProtocol {
        fun bar(input: String): String
    }

    @OpenForTest
    class B : BProtocol {
        override fun bar(input: String): String = input
    }

    @OpenForTest
    class C(
        val a: A,
        val b: BProtocol
    ) {
        fun join(): String = a.foo("a") + b.bar("b")
    }

    // Mock
    @Test
    fun `strict mode returns defined answers with parameter matching`() = runTest {
        val mock = mock<A> {
            every { foo(any()) } returns "DEFAULT"
            every { foo("y") } returns "Y"
        }

        every { mock.foo("x") } returns "X"

        assertEquals("Y", mock.foo("y"))
        assertEquals("X", mock.foo("x"))
        assertEquals("DEFAULT", mock.foo("z"))
        assertEquals("DEFAULT", mock.foo("abc"))
    }

    @Test
    fun `strict mode throws on missing answer`() = runTest {
        val mock = mock<A> { every { foo("x") } returns "X" }
        assertFailsWith<MokkeryRuntimeException> { mock.foo("y") }
    }

    @Test
    fun `autoUnit returns Unit for Unit functions without explicit answer`() {
        val mock = mock<A>(autoUnit)
        mock.returnUnit()
    }

    @Test
    fun `autofill mode returns default values`() = runTest {
        val mock = mock<A>(autofill)
        assertEquals("", mock.foo("anything"))
    }

    @Test
    fun `original mode calls actual implementation`() = runTest {
        val mock = mock<A>(original) { every { foo("x") } returns "X"  }

        assertEquals("test", mock.foo("test")) // use the real code implementation
        assertEquals("X", mock.foo("x")) // use the stub response
    }

    // Returns
    @Test
    fun `mock returns sequence answers`() = runTest {
        val mock = mock<A> {
            every { foo(any()) } sequentially {
                returns("DEFAULT")
                returns("SECOND")
                returns("THIRD")
            }
        }

        assertEquals("DEFAULT", mock.foo("z"))
        assertEquals("SECOND", mock.foo("z"))
        assertEquals("THIRD", mock.foo("z"))
        assertFailsWith<MokkeryRuntimeException> { mock.foo("MORE") }
    }

    @Test
    fun `mock returns calls answers with custom fallback`() = runTest {
        var counter = 0
        val mock = mock<A> {
            every { foo(any()) } calls { (input: String) ->
                counter++
                when (counter) {
                    1 -> "DEFAULT"
                    2 -> "SECOND"
                    3 -> "THIRD"
                    else -> input
                }
            }
        }

        assertEquals("DEFAULT", mock.foo("z"))
        assertEquals("SECOND", mock.foo("z"))
        assertEquals("THIRD", mock.foo("z"))
        assertEquals("MORE", mock.foo("MORE"))
    }

    @Test
    fun `mock returnsBy allows to execute function without argument`() = runTest {
        var counter = 0
        val mockA = mock<A> {
            every { foo(any()) } returnsBy { (counter++).toString() }
        }

        assertEquals("0", mockA.foo(""))
        assertEquals("1", mockA.foo(""))
        assertEquals("2", mockA.foo(""))
    }

    // Capture
    @Test
    fun `argument capturing with Capture`() = runTest {
        val mock = mock<A>()
        val slot = Capture.slot<String>()
        every { mock.foo(capture(slot)) } returns "captured"

        assertEquals("captured", mock.foo("test"))
        assertEquals("test", slot.get())
    }

    @Test
    fun `argument capturing with returnsArgAt return argument at index`() = runTest {
        val mockA = mock<A> {
            every { foo(any(), any()) } returnsArgAt 1
        }

        assertEquals("second", mockA.foo("", "second"))
    }

    // Misc.
    @Test
    fun `throws and throwsBy`() = runTest {
        val mockA = mock<A> {
            every { foo("fail") } throws IllegalStateException("fail")
            every { foo("fail with function") } throwsBy { IllegalArgumentException("by lambda") }
        }

        val ex1 = assertFailsWith<IllegalStateException> { mockA.foo("fail") }
        assertEquals("fail", ex1.message)

        val ex2 = assertFailsWith<IllegalArgumentException> { mockA.foo("fail with function") }
        assertEquals("by lambda", ex2.message)
    }

    @Test
    fun `mock many types`() = runTest {
        val mock = mockMany<A, BProtocol> {
            every { t1.foo(any()) } returns "aMock"
            every { t2.bar(any()) } returns "bMock"
        }

        assertEquals("aMock", mock.t1.foo("x"))
        assertEquals("bMock", mock.t2.bar("y"))
    }

    // Spy
    @Test
    fun `spying allow to mock some response or use original`() = runTest {
        val realA = A()
        val spyA = spy(realA)
        every { spyA.foo("mocked") } returns "spied"

        assertEquals("spied", spyA.foo("mocked")) // call the mocked response
        assertEquals("real", spyA.foo("real")) //call the real implementation of A
    }

    @Test
    fun `spying allow to verify on real instance`() = runTest {
        val spyB = spy(B())
        val realCWithMock = C(a = mock(original), b = spyB)
        assertEquals("ab", realCWithMock.join())

        verify {
            spyB.bar("b")
        }
    }

    // Verify
    @Test
    fun `verify example with counts`() = runTest {
        val mockA = mock<A> {
            every { foo(any()) } returns "val"
        }

        mockA.foo("one")

        mockA.foo("two")
        mockA.foo("two")

        mockA.foo("three")
        mockA.foo("three")
        mockA.foo("three")

        mockA.foo("four")
        mockA.foo("four")

        verify { mockA.foo("one") }
        verify(atLeast(2)) { mockA.foo("two") }
        verify(atMost(3)) { mockA.foo("three") }
        verify(inRange(1..3)) { mockA.foo("four") }
    }

    @Test
    fun `verify order`() = runTest {
        val mockA = mock<A> {
            every { foo(any()) } returns "val"
        }

        mockA.foo("first")
        mockA.foo("second")
        mockA.foo("third")

        verify(order) {
            mockA.foo("first")
            mockA.foo("second")
            // third not verified
        }

        // verifyNoMoreCalls(mockA) will fail because third was not verified.
    }

    @Test
    fun `verify order and exhaustive`() = runTest {
        val mockA = mock<A> {
            every { foo(any()) } returns "val"
        }

        mockA.foo("first")
        mockA.foo("second")
        mockA.foo("third")

        verify(exhaustive) {
            mockA.foo("first")
            mockA.foo("second")
            mockA.foo("third") // fail if we don't verify everything
        }
    }

    @Test
    fun `verifyNoMoreCalls and resetCalls example`() = runTest {
        val mockA = mock<A> {
            every { foo(any()) } returns "val"
        }

        mockA.foo("one")
        mockA.foo("two")

        verify { mockA.foo("one") }
        assertFailsWith<AssertionError> {
            verifyNoMoreCalls(mockA)
        }

        verify { mockA.foo("two") }
        verifyNoMoreCalls(mockA)

        mockA.foo("three")

        resetCalls(mockA)
        // reset all call, useful when you need some call to prepare your test
        // but verifying these call are not relevant to the test
        verifyNoMoreCalls(mockA)
    }

    // Matcher
    @Test
    fun `stub with matcher`() = runTest {
        val mockA = mock<A>()

        every { mockA.foo(matches { it.startsWith("pre") }) } returns "matchedValue"
        every { mockA.foo(matches { it.length > 10 }) } returns "longValue"

        assertEquals("matchedValue", mockA.foo("prefix"))
        assertEquals("longValue", mockA.foo("123456-abcdefg"))
    }

    @Test
    fun `verify with matcher only`() = runTest {
        val mockA = mock<A> {
            every { foo(any()) } returns ""
        }

        mockA.foo("prefix")
        mockA.foo("pre123")

        verify { mockA.foo(matches { input: String -> input.startsWith("pre") }) }
    }

    // Debug
    @Test
    fun `debugging mock`() = runTest {
        val a = mock<A>()
        every { a.foo(any()) } returns "ok"
        a.foo("test")
        printMokkeryDebug(a)
    }

    // Coroutine mokkery dependencies
    @Test
    fun `suspend function returns delayed return`() = runTest {
        val a = mock<A>()
        everySuspend { a.suspendedFoo(any()) } awaits delayed(value = "done")

        assertEquals("done", a.suspendedFoo("x"))
    }

    @Test
    fun `suspend function returns suspend calls block`() = runTest {
        val a = mock<A>()
        everySuspend { a.suspendedFoo(any()) } calls { (input: String) ->
            delay(50)
            "done"
        }

        assertEquals("done", a.suspendedFoo("x"))
    }

    @Test
    fun `suspend function returning deferred value`() = runTest {
        val a = mock<A>()
        val deferred = CompletableDeferred<String>()
        everySuspend { a.suspendedFoo(any()) } calls { (input: String) ->
            deferred.await()
        }

        deferred.complete("ok")
        assertEquals("ok", a.suspendedFoo("x"))
    }

    @Test
    fun `suspend function receiving from channel`() = runTest {
        val a = mock<A>()
        val channel = Channel<String>()

        everySuspend { a.suspendedFoo(any()) } calls { (input: String) ->
            channel.receive()
        }

        launch { channel.send("chan") }
        assertEquals("chan", a.suspendedFoo("x"))
    }

    @Test
    fun `suspend function returns flow`() = runTest {
        val a = mock<A>()

        every { a.flowFoo(any()) } calls { (input: String) ->
            flow {
                emit("first-$input")
                emit("second-$input")
            }
        }

        val results = a.flowFoo("x").toList()
        assertEquals(listOf("first-x", "second-x"), results)
    }

}