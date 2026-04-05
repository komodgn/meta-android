package com.metasearch.android.domain.person.api.usecase

import com.metasearch.android.data.domain.Person

interface DeletePersonUseCase {
    suspend operator fun invoke(person: Person): Result<Unit>
}
