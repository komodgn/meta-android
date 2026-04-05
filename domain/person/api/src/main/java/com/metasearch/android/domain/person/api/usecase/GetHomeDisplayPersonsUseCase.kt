package com.metasearch.android.domain.person.api.usecase

import com.metasearch.android.data.domain.Person
import kotlinx.coroutines.flow.Flow

interface GetHomeDisplayPersonsUseCase {
    operator fun invoke(): Flow<List<Person>>

    suspend fun syncAndGet(currentList: List<Person>): List<Person>
}
