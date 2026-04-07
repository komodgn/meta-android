package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.GetPersonDetailUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow

@SingleIn(DataScope::class)
@Inject
class GetPersonDetailUseCaseImpl(
    private val personRepository: PersonRepository,
) : GetPersonDetailUseCase {

    override fun invoke(personId: Long): Flow<Person?> {
        return personRepository.getPersonById(personId)
    }
}
