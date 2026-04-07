package com.metasearch.android.data.person.impl.di

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.person.impl.repository.ContactRepositoryImpl
import com.metasearch.android.data.person.impl.repository.PersonRepositoryImpl
import com.metasearch.android.data.person.impl.usecase.CheckNameExistsUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.DeletePersonUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.GetAllPersonsUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.GetHomeDisplayPersonsUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.GetPersonDetailUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.GetPersonPhotosUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.UpdatePersonInfoUseCaseImpl
import com.metasearch.android.data.person.impl.usecase.UpdateRepresentativeFaceUseCaseImpl
import com.metasearch.android.domain.person.api.repository.ContactRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.CheckNameExistsUseCase
import com.metasearch.android.domain.person.api.usecase.DeletePersonUseCase
import com.metasearch.android.domain.person.api.usecase.GetAllPersonsUseCase
import com.metasearch.android.domain.person.api.usecase.GetHomeDisplayPersonsUseCase
import com.metasearch.android.domain.person.api.usecase.GetPersonDetailUseCase
import com.metasearch.android.domain.person.api.usecase.GetPersonPhotosUseCase
import com.metasearch.android.domain.person.api.usecase.UpdatePersonInfoUseCase
import com.metasearch.android.domain.person.api.usecase.UpdateRepresentativeFaceUseCase
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
interface PersonDataGraph {
    @Binds
    val ContactRepositoryImpl.bind: ContactRepository

    @Binds
    val PersonRepositoryImpl.bind: PersonRepository

    @Binds
    val CheckNameExistsUseCaseImpl.bind: CheckNameExistsUseCase

    @Binds
    val DeletePersonUseCaseImpl.bind: DeletePersonUseCase

    @Binds
    val GetAllPersonsUseCaseImpl.bind: GetAllPersonsUseCase

    @Binds
    val GetHomeDisplayPersonsUseCaseImpl.bind: GetHomeDisplayPersonsUseCase

    @Binds
    val GetPersonDetailUseCaseImpl.bind: GetPersonDetailUseCase

    @Binds
    val GetPersonPhotosUseCaseImpl.bind: GetPersonPhotosUseCase

    @Binds
    val UpdatePersonInfoUseCaseImpl.bind: UpdatePersonInfoUseCase

    @Binds
    val UpdateRepresentativeFaceUseCaseImpl.bind: UpdateRepresentativeFaceUseCase
}
