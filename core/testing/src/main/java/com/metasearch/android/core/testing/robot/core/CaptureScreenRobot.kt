package com.metasearch.android.core.testing.robot.core

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.DefaultFileNameGenerator
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.captureRoboImage
import com.metasearch.android.core.testing.behavior.TestCaseDescriptionProvider
import dev.zacsweers.metro.Inject

@OptIn(ExperimentalTestApi::class)
interface CaptureScreenRobot {
    context(composeUiTest: ComposeUiTest, checkNode: TestCaseDescriptionProvider)
    fun captureScreenWithChecks(checks: () -> Unit)
}

@OptIn(ExperimentalTestApi::class, InternalRoborazziApi::class)
context(composeUiTest: ComposeUiTest)
fun SemanticsNodeInteraction.captureNodeWithDescription(description: String) {
    val filePath = DefaultFileNameGenerator.generateFilePath()
        .split(".")
        .dropLast(2) // Remove method name and ext.
        .joinToString(".")
        .plus(" - $description.png")

    this.captureRoboImage(filePath)
}

@Inject
@OptIn(ExperimentalTestApi::class)
class DefaultCaptureScreenRobot : CaptureScreenRobot {
    context(composeUiTest: ComposeUiTest, testCaseDescriptionProvider: TestCaseDescriptionProvider)
    override fun captureScreenWithChecks(checks: () -> Unit) {
        checks()
        composeUiTest.onRoot().captureNodeWithDescription(testCaseDescriptionProvider.description)
    }
}
