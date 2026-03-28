package com.metasearch.android.core.testing.robot.search

import com.metasearch.android.core.testing.repository.FakeSearchRepository
import dev.zacsweers.metro.Inject

interface SearchServerRobot {
    enum class ServerStatus {
        Success,
        Empty,
        Error,
    }

    fun setupSearchServer(status: ServerStatus)
}

@Inject
class DefaultSearchServerRobot(
    private val fakeSearchRepository: FakeSearchRepository,
) : SearchServerRobot {

    override fun setupSearchServer(status: SearchServerRobot.ServerStatus) {
        fakeSearchRepository.setup(
            when (status) {
                SearchServerRobot.ServerStatus.Success -> FakeSearchRepository.Status.Success
                SearchServerRobot.ServerStatus.Error -> FakeSearchRepository.Status.Error
                SearchServerRobot.ServerStatus.Empty -> FakeSearchRepository.Status.Empty
            },
        )
    }
}
