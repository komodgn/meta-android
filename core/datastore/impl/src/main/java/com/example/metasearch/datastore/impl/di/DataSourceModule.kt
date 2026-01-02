package com.example.metasearch.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.metasearch.core.datastore.api.datasource.DeviceIdDataSource
import com.example.metasearch.core.datastore.api.datasource.PersonIndexDataSource
import com.example.metasearch.datastore.impl.datasource.DeviceIdDataSourceImpl
import com.example.metasearch.datastore.impl.datasource.PersonIndexDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    private val Context.deviceIdDataStore by preferencesDataStore(name = "DEVICE_ID_DATASTORE")
    private val Context.personIndexDataStore by preferencesDataStore(name = "PERSON_INDEX_DATASTORE")

    @DeviceDatastore
    @Provides
    @Singleton
    fun provideDeviceIdDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.deviceIdDataStore

    @PersonIndexDatastore
    @Provides
    @Singleton
    fun personIndexDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.personIndexDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindDataStoreModule {
    @Binds
    @Singleton
    abstract fun bindDeviceIdDataSource(deviceIdDataSourceImpl: DeviceIdDataSourceImpl): DeviceIdDataSource

    @Binds
    @Singleton
    abstract fun bindPersonIndexDataSource(personIndexDataSourceImpl: PersonIndexDataSourceImpl): PersonIndexDataSource
}
