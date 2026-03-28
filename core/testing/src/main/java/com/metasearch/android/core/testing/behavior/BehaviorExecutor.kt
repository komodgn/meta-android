package com.metasearch.android.core.testing.behavior

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi

@OptIn(ExperimentalTestApi::class)
context(_: ComposeUiTest)
fun <T> Behavior<T>.execute(robot: T) {
    this.steps.forEach { step ->
        when (step) {
            is Step.Describe -> {
                executeSteps(robot, step.steps)
            }
            is Step.DoIt -> {
                step.action(robot)
            }
            is Step.ItShould -> {
                val descriptionProvider = TestCaseDescriptionProvider(step.description)
                with(descriptionProvider) {
                    step.assertion(robot)
                }
            }
        }
    }
}

@OptIn(ExperimentalTestApi::class)
context(_ : ComposeUiTest)
private fun <T> executeSteps(robot: T, steps: List<Step<T>>) {
    steps.forEach { step ->
        when (step) {
            is Step.Describe -> executeSteps(robot, step.steps)
            is Step.DoIt -> step.action(robot)
            is Step.ItShould -> {
                with(TestCaseDescriptionProvider(step.description)) {
                    step.assertion(robot)
                }
            }
        }
    }
}

class TestCaseDescriptionProvider(val description: String)
