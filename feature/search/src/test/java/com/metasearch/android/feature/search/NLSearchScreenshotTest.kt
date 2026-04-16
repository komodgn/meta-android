package com.metasearch.android.feature.search

import com.github.takahirom.roborazzi.captureRoboImage
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.testing.annotation.ComposeTest
import com.metasearch.android.core.testing.annotation.UiTestRunner
import com.metasearch.android.core.testing.rule.CoilRule
import com.metasearch.android.feature.search.nls.NLSearchUi
import com.metasearch.android.feature.search.nls.NLSearchUiState
import com.metasearch.android.feature.search.nls.mock.mock
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(UiTestRunner::class)
class NLSearchScreenshotTest {

    @get:Rule
    val coilRule = CoilRule()

    @ComposeTest
    fun captureNLSearchInitialState() {
        captureRoboImage(
            "screenshots/nl_search_initial_state.png",
        ) {
            MetaSearchTheme {
                NLSearchUi(state = NLSearchUiState.mock())
            }
        }
    }
}
