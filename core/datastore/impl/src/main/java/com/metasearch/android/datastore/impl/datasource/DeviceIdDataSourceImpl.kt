package com.metasearch.android.datastore.impl.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.metasearch.android.core.datastore.api.datasource.DeviceIdDataSource
import com.metasearch.android.datastore.impl.di.DeviceDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeviceIdDataSourceImpl @Inject constructor(
    @DeviceDatastore private val deviceIdDataSource: DataStore<Preferences>,
) : DeviceIdDataSource {
    override val deviceId: Flow<String> = deviceIdDataSource.data.map {
        it[PreferencesKeys.DEVICE_ID] ?: ""
    }

    override suspend fun getDeviceId(): String = deviceId.first()

    override suspend fun setDeviceId(deviceId: String) {
        deviceIdDataSource.edit { preferences ->
            preferences[PreferencesKeys.DEVICE_ID] = deviceId
        }
    }

    object PreferencesKeys {
        val DEVICE_ID = stringPreferencesKey("device_id")
    }
}
