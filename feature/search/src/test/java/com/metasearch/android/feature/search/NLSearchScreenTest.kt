package com.metasearch.android.feature.search

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.metasearch.android.core.testing.annotation.ComposeTest
import com.metasearch.android.core.testing.annotation.RunWith
import com.metasearch.android.core.testing.annotation.UiTestRunner
import com.metasearch.android.core.testing.behavior.describeBehaviors
import com.metasearch.android.core.testing.behavior.execute
import com.metasearch.android.core.testing.di.createSearchScreenTestGraph
import com.metasearch.android.core.testing.robot.search.NLSearchScreenRobot
import com.metasearch.android.core.testing.robot.search.SearchServerRobot
import com.metasearch.android.core.testing.rule.CoilRule
import org.junit.Rule

@RunWith(UiTestRunner::class)
class NLSearchScreenTest {

    @get:Rule
    val coilRule = CoilRule()

    val testAppGraph = createSearchScreenTestGraph()

    @OptIn(ExperimentalTestApi::class)
    @ComposeTest
    fun runTest() {
        listOf(describedBehaviors).forEach { behavior ->
            val robot = testAppGraph.nLSearchScreenRobotProvider.invoke()

            runComposeUiTest {
                behavior.execute(robot)
            }
        }
    }

    val describedBehaviors = describeBehaviors<NLSearchScreenRobot>("NLSearchScreen") {
        describe("when the server is operating normally") {
            doIt {
                setupSearchServer(SearchServerRobot.ServerStatus.Success)
                setContent()
            }

            describe("when the user enters a query and checks the results") {
                itShould("display the search result list correctly") {
                    captureScreenWithChecks {
                        checkAnyImageDisplayed()
                    }
                }
            }
        }
    }
}
