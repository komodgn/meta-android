package com.metasearch.android.core.datastore.api.datasource

import kotlinx.coroutines.flow.Flow

interface PersonIndexDataSource {
    val lastPersonIndex: Flow<Int>

    suspend fun getLastPersonIndex(): Int
    suspend fun setLastPersonIndex(index: Int)
}
