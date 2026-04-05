package com.metasearch.android.domain.person.api.repository

interface ContactRepository {
    suspend fun getCallDurations(): Map<String, Long>
}
