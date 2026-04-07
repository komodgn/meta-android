package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.CheckNameExistsUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class CheckNameExistsUseCaseImpl(
    private val personRepository: PersonRepository,
) : CheckNameExistsUseCase {

    override suspend fun invoke(name: String): Boolean {
        return personRepository.isNameExists(name)
    }
}
