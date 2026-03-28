package com.metasearch.android.core.testing.behavior

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi

interface Behavior<T> {
    val description: String
    val steps: List<Step<T>>
}

@OptIn(ExperimentalTestApi::class)
sealed interface Step<T> {
    data class Describe<T>(val description: String, val steps: List<Step<T>>) : Step<T>

    data class DoIt<T>(
        val action: context(ComposeUiTest) T.() -> Unit,
    ) : Step<T>

    data class ItShould<T>(
        val description: String,
        val assertion: context(ComposeUiTest, TestCaseDescriptionProvider) T.() -> Unit,
    ) : Step<T>
}

@OptIn(ExperimentalTestApi::class)
class BehaviorBuilder<T>(private val description: String) {
    val steps = mutableListOf<Step<T>>()

    fun describe(description: String, block: BehaviorBuilder<T>.() -> Unit) {
        val builder = BehaviorBuilder<T>(description)
        builder.block()
        steps.add(Step.Describe(description, builder.steps))
    }

    fun doIt(action: context(ComposeUiTest) T.() -> Unit) {
        steps.add(Step.DoIt(action))
    }

    fun itShould(
        description: String,
        assertion: context(ComposeUiTest, TestCaseDescriptionProvider) T.() -> Unit,
    ) {
        steps.add(Step.ItShould(description, assertion))
    }

    fun build(): Behavior<T> = object : Behavior<T> {
        override val description = this@BehaviorBuilder.description
        override val steps = this@BehaviorBuilder.steps
    }
}

fun <T> describeBehaviors(
    description: String,
    block: BehaviorBuilder<T>.() -> Unit,
): Behavior<T> {
    return BehaviorBuilder<T>(description).apply(block).build()
}
