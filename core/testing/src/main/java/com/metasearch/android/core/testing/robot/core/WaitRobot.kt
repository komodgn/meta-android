package com.metasearch.android.core.testing.robot.core

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import org.robolectric.shadows.ShadowLooper

@OptIn(ExperimentalTestApi::class)
interface WaitRobot {
    context(composeUiTest: ComposeUiTest)
    fun waitUntilIdle()

    context(composeUiTest: ComposeUiTest)
    fun waitFor5Seconds()
}

@OptIn(ExperimentalTestApi::class)
@Inject
class DefaultWaitRobot(
    private val testDispatcher: TestDispatcher,
) : WaitRobot {

    context(composeUiTest: ComposeUiTest)
    override fun waitUntilIdle() {
        composeUiTest.waitForIdle()

        (testDispatcher.scheduler as? TestCoroutineScheduler)?.advanceUntilIdle()

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    context(composeUiTest: ComposeUiTest)
    override fun waitFor5Seconds() {
        repeat(5) {
            composeUiTest.mainClock.advanceTimeBy(1000)
            (testDispatcher.scheduler as? TestCoroutineScheduler)?.advanceTimeBy(1000)
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
    }
}
