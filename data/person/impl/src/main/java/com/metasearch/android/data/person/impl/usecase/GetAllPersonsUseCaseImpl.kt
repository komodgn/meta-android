package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.GetAllPersonsUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow

@SingleIn(DataScope::class)
@Inject
class GetAllPersonsUseCaseImpl(
    private val personRepository: PersonRepository,
) : GetAllPersonsUseCase {

    override fun invoke(): Flow<List<Person>> = personRepository.getAllPersons()
}
