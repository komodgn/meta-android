package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.common.utils.runSuspendCatching
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.DeletePersonUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@SingleIn(DataScope::class)
@Inject
class DeletePersonUseCaseImpl(
    private val personRepository: PersonRepository,
) : DeletePersonUseCase {

    override suspend fun invoke(person: Person): Result<Unit> = runSuspendCatching {
        coroutineScope {
            val webDelete = async { personRepository.deleteFromWebService(person.inputName) }
            val aiDelete = async { personRepository.deleteFromAiService(person.name) }

            awaitAll(webDelete, aiDelete).forEach {
                it.getOrThrow()
            }
        }

        personRepository.deleteFromLocalDb(person.id).getOrThrow()
    }
}
