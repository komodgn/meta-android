package com.metasearch.android.core.testing.robot.search

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onFirst
import com.metasearch.android.core.testing.compose.MetaSearchTestContainer
import com.metasearch.android.core.testing.robot.core.CaptureScreenRobot
import com.metasearch.android.core.testing.robot.core.DefaultWaitRobot
import com.metasearch.android.core.testing.robot.core.WaitRobot
import com.metasearch.android.feature.search.nls.NLSearchResultItemTestTagPrefix
import com.metasearch.android.feature.search.nls.NLSearchUi
import com.metasearch.android.feature.search.nls.NLSearchUiState
import com.metasearch.android.feature.search.nls.mock.mock
import dev.zacsweers.metro.Inject

@Inject
@OptIn(ExperimentalTestApi::class)
class NLSearchScreenRobot(
    searchServerRobot: DefaultSearchServerRobot,
    captureScreenRobot: CaptureScreenRobot,
    waitRobot: DefaultWaitRobot,
) : SearchServerRobot by searchServerRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot {

    context(composeUiTest: ComposeUiTest)
    fun setContent() {
        composeUiTest.setContent {
            MetaSearchTestContainer {
                NLSearchUi(
                    state = NLSearchUiState.mock(),
                )
            }
        }

        waitUntilIdle()
    }

    context(composeUiTest: ComposeUiTest)
    fun checkAnyImageDisplayed() {
        waitUntilIdle()

        val prefixMatcher = SemanticsMatcher("TestTag starts with $NLSearchResultItemTestTagPrefix") { node ->
            val tag = node.config.getOrNull(SemanticsProperties.TestTag)
            tag?.startsWith(NLSearchResultItemTestTagPrefix) == true
        }

        composeUiTest
            .onAllNodes(prefixMatcher, useUnmergedTree = true)
            .onFirst()
            .assertIsDisplayed()
    }
}
