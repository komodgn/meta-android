package com.metasearch.android.core.testing.repository

import com.metasearch.android.domain.graph.api.repository.GraphRepository
import dev.zacsweers.metro.Inject

@Inject
public class FakeGraphRepository : GraphRepository {

    public sealed class Status {
        public data object Success : Status()
        public data object Empty : Status()
        public data object Error : Status()
    }

    private var status: Status = Status.Success

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun getFullGraphWebViewUrl(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailGraphWebViewUrl(entityName: String): String {
        TODO("Not yet implemented")
    }
}
