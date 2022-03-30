package com.example.compose.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.compose.local.preferences.AppPreferences
import com.example.compose.local.preferences.dataStore
import com.example.compose.local.room.DataBase
import com.example.compose.local.room.ModificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun provideDataStore(@ApplicationContext c: Context) = c.dataStore

    @Singleton
    @Provides
    fun providePreferences(dataStore: DataStore<Preferences>) = AppPreferences(dataStore)

    @Provides
    fun provideSortPrefs(prefsRepository: AppPreferences) = prefsRepository.sortOrdersFlow

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext c: Context) =
        Room.databaseBuilder(c, DataBase::class.java, "song_database").build()

    @Provides
    fun provideModificationDao(dataBase: DataBase): ModificationDao = dataBase.modificationDao

}