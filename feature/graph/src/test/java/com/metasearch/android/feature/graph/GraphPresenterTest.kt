package com.metasearch.android.feature.graph

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.domain.graph.api.usecase.GetFullGraphUrlUseCase
import com.metasearch.android.domain.graph.api.usecase.GetGraphImageUriUseCase
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.PersonScreen
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GraphPresenterTest {

    private val navigator = FakeNavigator(GraphScreen)
    private val getFullGraphUrlUseCase: GetFullGraphUrlUseCase = mock()
    private val getGraphImageUriUseCase: GetGraphImageUriUseCase = mock()

    private lateinit var presenter: GraphPresenter

    @Before
    fun setup() {
        presenter = GraphPresenter(navigator, getFullGraphUrlUseCase, getGraphImageUriUseCase)
    }

    @Test
    fun `should reset root when tab is clicked`() = runTest {
        val tabs = listOf(PersonScreen, HomeScreen, NLSearchScreen, GraphScreen)

        whenever(getFullGraphUrlUseCase.invoke()).thenAnswer { "https://www.google.com/" }

        tabs.forEach { targetScreen ->
            presenter.test {
                var state = awaitItem()

                if (state.webViewUrl.isEmpty()) {
                    state = awaitItem()
                }

                state.eventSink(GraphUiEvent.OnTabClick(targetScreen))

                val resetEvent = navigator.awaitResetRoot()
                assertThat(resetEvent.newRoot).isEqualTo(targetScreen)
            }
        }
    }
}
