package com.metasearch.android.domain.person.api.usecase

import com.metasearch.android.data.domain.Person
import kotlinx.coroutines.flow.Flow

interface GetPersonDetailUseCase {
    operator fun invoke(personId: Long): Flow<Person?>
}
