package com.metasearch.android.feature.home

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.metasearch.android.core.worker.api.usecase.WorkScheduleUseCase
import com.metasearch.android.core.worker.api.usecase.WorkerStatusUseCase
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.gallery.api.repository.GalleryRepository
import com.metasearch.android.domain.person.api.usecase.GetHomeDisplayPersonsUseCase
import com.metasearch.android.feature.home.worker.ImageAnalysisWorker
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.PersonDetailScreen
import com.metasearch.android.feature.screens.PersonScreen
import com.metasearch.android.feature.screens.PhotoDetailScreen
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomePresenterTest {

    private val navigator = FakeNavigator(HomeScreen)
    private val galleryRepository: GalleryRepository = mock()
    private val getHomeDisplayPersonsUseCase: GetHomeDisplayPersonsUseCase = mock()
    private val workScheduleUseCase: WorkScheduleUseCase = mock()
    private val workerStatusUseCase: WorkerStatusUseCase = mock()

    private lateinit var presenter: HomePresenter

    @Before
    fun setup() {
        whenever(galleryRepository.getGalleryPagingData()).thenReturn(flowOf(PagingData.empty()))
        whenever(getHomeDisplayPersonsUseCase()).thenReturn(flowOf(emptyList()))
        whenever(workerStatusUseCase.monitoringUniqueJobStatus(any())).thenReturn(flowOf(null))

        presenter = HomePresenter(
            navigator,
            galleryRepository,
            getHomeDisplayPersonsUseCase,
            workScheduleUseCase,
            workerStatusUseCase,
        )
    }

    @Test
    fun `should expand person section and sync data when OnPersonSectionExpand is clicked`() = runTest {
        val syncedList = listOf(mock<Person>())
        whenever(getHomeDisplayPersonsUseCase.syncAndGet(any())).thenReturn(syncedList)

        presenter.test {
            val initialState = awaitItem()
            initialState.eventSink(HomeUiEvent.OnPersonSectionExpand)

            val loadingState = awaitItem()
            assertThat(loadingState.isExpanded).isTrue()

            val finalState = awaitItem()
            assertThat(finalState.persons).hasSize(1)
            assertThat(finalState.isPersonLoading).isFalse()
        }
    }

    @Test
    fun `should navigate to PersonDetailScreen when face image is clicked`() = runTest {
        presenter.test {
            val initialState = awaitItem()

            initialState.eventSink(HomeUiEvent.OnPersonClick(1L))

            assertThat(navigator.awaitNextScreen())
                .isEqualTo(PersonDetailScreen(1L))
        }
    }

    @Test
    fun `should navigate to PhotoDetailScreen when image is clicked`() = runTest {
        presenter.test {
            val initialState = awaitItem()

            initialState.eventSink(HomeUiEvent.OnImageClick("uri_string"))

            assertThat(navigator.awaitNextScreen())
                .isEqualTo(PhotoDetailScreen("uri_string"))
        }
    }

    @Test
    fun `should set ShareImage side effect when OnShareRelease is invoked`() = runTest {
        presenter.test {
            val initialState = awaitItem()
            val testUri = "test_uri"

            initialState.eventSink(HomeUiEvent.OnShareRelease(testUri))

            val stateWithEffect = awaitItem()
            assertThat(stateWithEffect.sideEffect).isEqualTo(HomeSideEffect.ShareImage(testUri))
            assertThat(stateWithEffect.selectedLongClickImage).isNull()
        }
    }

    @Test
    fun `should trigger ImageAnalysisWorker when start analysis button is clicked`() = runTest {
        presenter.test {
            val state = awaitItem()
            state.eventSink(HomeUiEvent.OnStartAnalysisClicked)

            verify(workScheduleUseCase).scheduleNow(
                workName = eq("ImageAnalysisWork"),
                klass = eq(ImageAnalysisWorker::class),
                options = any(),
                params = any(),
            )
        }
    }

    @Test
    fun `should reset root when tab is clicked`() = runTest {
        val tabs = listOf(PersonScreen, HomeScreen, NLSearchScreen, GraphScreen)

        tabs.forEach { targetScreen ->
            presenter.test {
                val initialState = awaitItem()

                initialState.eventSink(HomeUiEvent.OnTabClick(targetScreen))

                val resetEvent = navigator.awaitResetRoot()
                assertThat(resetEvent.newRoot).isEqualTo(targetScreen)
            }
        }
    }
}
