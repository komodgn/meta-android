package com.metasearch.android.feature.search

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.domain.search.api.repository.ModelRepository
import com.metasearch.android.domain.search.api.repository.SearchRepository
import com.metasearch.android.domain.search.api.usecase.NLSearchUseCase
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.PersonScreen
import com.metasearch.android.feature.search.nls.NLSearchPresenter
import com.metasearch.android.feature.search.nls.NLSearchUiEvent
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NLSearchPresenterTest {

    private val navigator = FakeNavigator(NLSearchScreen)
    private val nlSearchUseCase: NLSearchUseCase = mock()
    private val searchRepository = mock<SearchRepository>()
    private val modelRepository = mock<ModelRepository>()

    private lateinit var presenter: NLSearchPresenter

    @Before
    fun setup() {
        whenever(modelRepository.getAllModels()).thenReturn(emptyList())
        whenever(searchRepository.isLocalModelAvailable(any())).thenReturn(false)

        presenter = NLSearchPresenter(
            navigator = navigator,
            nlSearchUseCase = nlSearchUseCase,
            searchRepository = searchRepository,
            modelRepository = modelRepository,
        )
    }

    @Test
    fun `should reset root when tab is clicked`() = runTest {
        val tabs = listOf(PersonScreen, HomeScreen, NLSearchScreen, GraphScreen)

        tabs.forEach { targetScreen ->
            presenter.test {
                val initialState = awaitItem()

                initialState.eventSink(NLSearchUiEvent.OnTabClick(targetScreen))

                val resetEvent = navigator.awaitResetRoot()
                assertThat(resetEvent.newRoot).isEqualTo(targetScreen)
            }
        }
    }
}
