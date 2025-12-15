package com.example.metasearch.datastore.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.metasearch.core.datastore.api.datasource.DeviceIdDataSource
import com.example.metasearch.datastore.impl.datasource.DeviceIdDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    private val Context.deviceIdDataStore by preferencesDataStore(name = "DEVICE_ID_DATASTORE")

    @DeviceDatastore
    @Provides
    @Singleton
    fun provideDeviceIdDataStore(context: Context): DataStore<Preferences> = context.deviceIdDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindDataStoreModule {
    @Binds
    @Singleton
    abstract fun bindDeviceIdDataSource(deviceIdDataSourceImpl: DeviceIdDataSourceImpl): DeviceIdDataSource
}
