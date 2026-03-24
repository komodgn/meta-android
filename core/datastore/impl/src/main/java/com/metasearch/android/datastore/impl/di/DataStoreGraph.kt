package com.metasearch.android.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.metasearch.android.core.datastore.api.datasource.DeviceIdDataSource
import com.metasearch.android.core.datastore.api.datasource.PersonIndexDataSource
import com.metasearch.android.datastore.impl.datasource.DeviceIdDataSourceImpl
import com.metasearch.android.datastore.impl.datasource.PersonIndexDataSourceImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

private val Context.deviceIdDataStore by preferencesDataStore(name = "DEVICE_ID_DATASTORE")
private val Context.personIndexDataStore by preferencesDataStore(name = "PERSON_INDEX_DATASTORE")

@ContributesTo(AppScope::class)
interface DataSourceGraph {

    @DeviceDatastore
    @Provides
    fun provideDeviceIdDataStore(
        context: Context,
    ): DataStore<Preferences> = context.deviceIdDataStore

    @PersonIndexDatastore
    @Provides
    fun personIndexDataStore(
        context: Context,
    ): DataStore<Preferences> = context.personIndexDataStore

    @Binds
    val DeviceIdDataSourceImpl.bind: DeviceIdDataSource

    @Binds
    val PersonIndexDataSourceImpl.bind: PersonIndexDataSource
}
