package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.UpdatePersonInfoUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(DataScope::class)
@Inject
class UpdatePersonInfoUseCaseImpl(
    private val personRepository: PersonRepository,
) : UpdatePersonInfoUseCase {

    override suspend fun invoke(
        personId: Long,
        newName: String,
        newPhone: String,
        isHome: Boolean,
        faceId: Long?,
    ): Result<Long> {
        return personRepository.updatePersonInfo(
            personId = personId,
            newName = newName,
            newPhone = newPhone,
            isHome = isHome,
            faceId = faceId,
        )
    }
}
