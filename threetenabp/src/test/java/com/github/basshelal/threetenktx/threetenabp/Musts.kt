@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package uk.whitecrescent.waqti

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable
import org.opentest4j.AssertionFailedError
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

// Basic

inline infix fun <T> T?.mustEqual(other: T?) {
    Assertions.assertEquals(this, other)
}

inline infix fun <T> T?.mustNotEqual(other: T?) {
    Assertions.assertNotEquals(this, other)
}

inline infix fun <T> T?.mustBeSameAs(other: T?) {
    Assertions.assertSame(this, other)
}

inline infix fun <T> T?.mustNotBeSameAs(other: T?) {
    Assertions.assertNotSame(this, other)
}

// Comparables

inline infix fun <T : Comparable<T>> T.mustBeLessThan(other: T) {
    (this < other) mustBe true
}

inline infix fun <T : Comparable<T>> T.mustBeLessThanOrEqualTo(other: T) {
    (this <= other) mustBe true
}

inline infix fun <T : Comparable<T>> T.mustBeGreaterThan(other: T) {
    (this > other) mustBe true
}

inline infix fun <T : Comparable<T>> T.mustBeGreaterThanOrEqualTo(other: T) {
    (this >= other) mustBe true
}

// Exceptions

inline infix fun <T : () -> Any?> T.mustThrow(exception: KClass<out Throwable>) {
    Assertions.assertThrows(exception.java, { this() })
}

inline infix fun <T : () -> Any?> T.mustThrowOnly(exception: KClass<out Throwable>) {
    try {
        this()
    } catch (t: Throwable) {
        if (t::class != exception) {
            throw AssertionFailedError(
                "Expected to throw only ${exception.simpleName} " +
                        "but actually did throw ${t::class.simpleName}", t
            )
        } else {
            t.printStackTrace()
        }
    }
}

/**
 * This function/block must not throw the given [Throwable], if it does an [AssertionFailedError]
 * is given and the test is failed.
 *
 * Any other Exceptions are still thrown, meaning the test can still fail as a result of these
 * Exceptions being thrown.
 *
 * Prefer this over [mustNotThrowOnly] as it is better practice to fail tests that throw any
 * exceptions.
 */
inline infix fun <T : () -> Any?> T.mustNotThrow(exception: KClass<out Throwable>) {
    try {
        this()
    } catch (t: Throwable) {
        if (t::class == exception || t::class.allSuperclasses.contains(exception)) {
            throw AssertionFailedError(
                "Expected not to throw ${exception.simpleName} " +
                        "but actually did throw ${t::class.simpleName}", t
            )
        } else {
            throw t
        }
    }
}

/**
 * This function/block must not throw the given [Throwable], if it does an [AssertionFailedError]
 * is given and the test is failed.
 *
 * **Any other Exceptions are caught and their stack trace is printed**
 *
 * Prefer [mustNotThrow] over this as it is better practice to fail tests that throw any
 * exceptions.
 */
inline infix fun <T : () -> Any?> T.mustNotThrowOnly(exception: KClass<out Throwable>) {
    try {
        this()
    } catch (t: Throwable) {
        if (t::class == exception || t::class.allSuperclasses.contains(exception)) {
            throw AssertionFailedError(
                "Expected not to throw ${exception.simpleName} " +
                        "but actually did throw ${t::class.simpleName}", t
            )
        } else {
            t.printStackTrace()
        }
    }
}

inline fun <T : () -> Any?> T.mustNotThrowAnyException() {
    Assertions.assertAll(Executable { this() })
}

// Booleans and Predicates

inline infix fun Boolean.mustBe(boolean: Boolean) {
    if (boolean) assertTrue else assertFalse
}

inline infix fun (() -> Boolean).mustBe(boolean: Boolean) {
    this.invoke() mustBe boolean
}

inline infix fun (() -> Boolean).mustBe(predicate: () -> Boolean) {
    this.invoke() mustBe predicate()
}

inline infix fun Boolean.mustBe(predicate: () -> Boolean) {
    this mustBe predicate()
}

// Functions

inline infix fun <reified R1, reified R2>
        (() -> R1).mustHaveSameResultAs(other: () -> R2) {
    this.invoke() mustEqual other.invoke()
}

inline infix fun <reified R1, reified R2>
        (() -> R1).mustNotHaveSameResultAs(other: () -> R2) {
    this.invoke() mustNotEqual other.invoke()
}

inline infix fun <reified R1, reified R2>
        (() -> R1).mustHaveSameResultType(other: () -> R2) {
    this.invoke() mustBeSameTypeAs other.invoke()
}

inline infix fun <reified R1, reified R2>
        (() -> R1).mustNotHaveSameResultType(other: () -> R2) {
    this.invoke() mustNotBeSameTypeAs other.invoke()
}

// Types

inline infix fun <reified T, reified R> T.mustBeSameTypeAs(other: R) {
    T::class mustEqual R::class
}

inline infix fun <reified T, reified R> T.mustNotBeSameTypeAs(other: R) {
    T::class mustNotEqual R::class
}

// Collections

inline fun <T> Collection<T>.mustBeEmpty() {
    this.isEmpty() mustBe true
}

inline fun <T> Collection<T>.mustNotBeEmpty() {
    this.isEmpty() mustBe false
}

inline infix fun <T> Collection<T>.mustHaveSizeOf(size: Int) {
    this.size mustEqual size
}

inline infix fun <T> Collection<T>.mustNotHaveSizeOf(size: Int) {
    this.size mustNotEqual size
}

inline infix fun <T> Collection<T>.mustHaveAllElementsEqualTo(other: T) {
    this.forEach { it mustEqual other }
}

inline infix fun <T> Collection<T>.mustHaveAllElementsNotEqualTo(other: T) {
    this.forEach { it mustNotEqual other }
}

// Ranges

inline infix fun <T : Comparable<T>> T.mustBeIn(range: ClosedRange<T>) {
    (this in range) mustBe true
}

// Scope Functions (not Musts)

/**
 * Runs the `first` block first then the `do` block, `first` is assumed to not throw any
 * Exceptions and if it does then the test is aborted and failed.
 *
 * Typical use for this is to run some code in the `first` block that will result in something
 * that will be tested in the `do` block, where the assertions will be.
 *
 * For example:
 * ```
 * after({ myInt + 1 }) {
 *     myInt mustEqual 1
 * }
 * ```
 */
inline fun <T : () -> Any?, R : () -> Any?> after(first: T, `do`: R) {
    first.mustNotThrowAnyException()
    `do`()
}

/**
 * Just a reversed apply, so instead of `task.apply{...}` we do
 * `on(task){...}`.
 * This just makes things a little more readable in tests when you're running multiple tests on
 * the same thing.
 * @see apply
 */
inline fun <T> on(element: T, func: T.() -> Unit) {
    element.apply(func)
}

// Utility values

inline val <T> T.ignoreResult: Unit
    get() = this as Unit

inline val <T> T?.assertNull
    get() = Assertions.assertNull(this)

inline val Boolean.assertTrue
    get() = Assertions.assertTrue(this)

inline val Boolean.assertFalse
    get() = Assertions.assertFalse(this)

inline val <T : Executable> Iterable<T>.assertAll
    get() = Assertions.assertAll(this.toList())