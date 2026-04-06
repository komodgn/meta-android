package com.metasearch.android.data.person.impl.usecase

import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.data.domain.Person
import com.metasearch.android.domain.person.api.repository.ContactRepository
import com.metasearch.android.domain.person.api.repository.PersonRepository
import com.metasearch.android.domain.person.api.usecase.GetHomeDisplayPersonsUseCase
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SingleIn(DataScope::class)
@Inject
class GetHomeDisplayPersonsUseCaseImpl(
    private val personRepository: PersonRepository,
    private val contactRepository: ContactRepository,
) : GetHomeDisplayPersonsUseCase {

    override fun invoke(): Flow<List<Person>> {
        return personRepository.getAllPersons().map { localPeople ->
            val callDurations = contactRepository.getCallDurations()

            val homeDisplayPeople = localPeople
                .filter { it.isHomeDisplay }
                .map { it.copy(totalDuration = callDurations[it.phoneNumber] ?: 0L) }

            normalizeScores(homeDisplayPeople)
        }
    }

    override suspend fun syncAndGet(currentList: List<Person>): List<Person> {
        val frequencies = personRepository.fetchPhotoFrequencies(currentList.map { it.inputName })
            .getOrDefault(emptyList())

        val updatedModels = currentList.map { person ->
            val matched = frequencies.find { it.personName == person.inputName }
            person.copy(photoCount = matched?.frequency ?: 0)
        }

        return normalizeScores(updatedModels)
    }

    private fun normalizeScores(people: List<Person>): List<Person> {
        if (people.isEmpty()) return emptyList()

        val maxPhotoCount = people.maxOf { it.photoCount }.coerceAtLeast(1)
        val maxDuration = people.maxOf { it.totalDuration }.coerceAtLeast(1L)

        return people.map { person ->
            val normalizedScore = (
                (person.photoCount.toDouble() / maxPhotoCount) +
                    (person.totalDuration.toDouble() / maxDuration)
                ) / 2.0
            person.copy(normalizedScore = normalizedScore)
        }.sortedByDescending { it.normalizedScore }
    }
}
