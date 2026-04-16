package com.metasearch.android.feature.person

import com.google.common.truth.Truth.assertThat
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.person.api.usecase.DeletePersonUseCase
import com.metasearch.android.domain.person.api.usecase.GetAllPersonsUseCase
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.PersonScreen
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PersonPresenterTest {

    private val navigator = FakeNavigator(PersonScreen)
    private val getAllPersonsUseCase: GetAllPersonsUseCase = mock()
    private val deletePersonUseCase: DeletePersonUseCase = mock()

    private lateinit var presenter: PersonPresenter

    @Before
    fun setup() {
        whenever(getAllPersonsUseCase()).thenReturn(flowOf(emptyList()))
        presenter = PersonPresenter(navigator, getAllPersonsUseCase, deletePersonUseCase)
    }

    @Test
    fun `should filter people list when input name changes`() = runTest {
        val mockPeople = listOf(
            Person(id = 1, name = "인물1", inputName = "Alice"),
            Person(id = 2, name = "인물2", inputName = "Bob"),
        )
        whenever(getAllPersonsUseCase()).thenReturn(flowOf(mockPeople))

        presenter.test {
            var currentState = awaitItem()
            if (currentState.people.isEmpty()) { currentState = awaitItem() }

            currentState.eventSink(PersonUiEvent.OnInputChange("Al"))

            val filteredState = awaitItem()
            assertThat(filteredState.people).hasSize(1)
            assertThat(filteredState.people[0].inputName).isEqualTo("Alice")
        }
    }

    @Test
    fun `should show delete dialog when delete button is clicked`() = runTest {
        val mockPeople = listOf(Person(id = 1, name = "인물1", inputName = "Alice"))
        whenever(getAllPersonsUseCase()).thenReturn(flowOf(mockPeople))

        presenter.test {
            var state = awaitItem()
            if (state.people.isEmpty()) {
                state = awaitItem()
            }

            state.eventSink(PersonUiEvent.OnPersonDeleteClick(1L))

            val dialogState = expectMostRecentItem()
            assertThat(dialogState.showDeleteDialog).isTrue()
            assertThat(dialogState.pendingDeletePersonName).isEqualTo("Alice")
        }
    }

    @Test
    fun `should reset root when tab is clicked`() = runTest {
        val tabs = listOf(PersonScreen, HomeScreen, NLSearchScreen, GraphScreen)

        tabs.forEach { targetScreen ->
            presenter.test {
                val initialState = awaitItem()

                initialState.eventSink(PersonUiEvent.OnTabClick(targetScreen))

                val resetEvent = navigator.awaitResetRoot()
                assertThat(resetEvent.newRoot).isEqualTo(targetScreen)
            }
        }
    }
}
