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

    private lateinit var navigator: FakeNavigator

    private val getAllPersonsUseCase: GetAllPersonsUseCase = mock()
    private val deletePersonUseCase: DeletePersonUseCase = mock()

    @Before
    fun setup() {
        navigator = FakeNavigator(PersonScreen)
    }

    @Test
    fun `should filter people list when input name changes`() = runTest {
        val mockPeople = listOf(
            Person(id = 1, name = "인물1", inputName = "Alice"),
            Person(id = 2, name = "인물2", inputName = "Bob"),
        )
        whenever(getAllPersonsUseCase()).thenReturn(flowOf(mockPeople))

        val presenter = createPresenter()

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

        val presenter = createPresenter()

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

        whenever(getAllPersonsUseCase()).thenReturn(flowOf(emptyList()))

        tabs.forEach { targetScreen ->
            val testNavigator = FakeNavigator(PersonScreen)
            val testPresenter = createPresenter(testNavigator)

            testPresenter.test {
                val initialState = awaitItem()

                initialState.eventSink(PersonUiEvent.OnTabClick(targetScreen))

                val resetEvent = testNavigator.awaitResetRoot()
                assertThat(resetEvent.newRoot).isEqualTo(targetScreen)
            }
        }
    }

    private fun createPresenter(testNavigator: FakeNavigator = navigator): PersonPresenter {
        return PersonPresenter(testNavigator, getAllPersonsUseCase, deletePersonUseCase)
    }
}
