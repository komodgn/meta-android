package com.metasearch.android.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.metasearch.android.core.datastore.api.datasource.PersonIndexDataSource
import com.metasearch.android.datastore.impl.di.DeviceDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonIndexDataSourceImpl @Inject constructor(
    @DeviceDatastore private val dataStore: DataStore<Preferences>,
) : PersonIndexDataSource {
    override val lastPersonIndex: Flow<Int> = dataStore.data.map {
        it[PreferencesKeys.LAST_PERSON_INDEX] ?: 0
    }

    override suspend fun getLastPersonIndex(): Int = lastPersonIndex.first()

    override suspend fun setLastPersonIndex(index: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_PERSON_INDEX] = index
        }
    }

    object PreferencesKeys {
        val LAST_PERSON_INDEX = intPreferencesKey("last_person_index")
    }
}
