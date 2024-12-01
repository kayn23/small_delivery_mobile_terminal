package com.kafpin.jwtauth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kafpin.jwtauth.data.dataStore.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(dataStore: DataStore<Preferences>): TokenManager {
        return TokenManager(dataStore)
    }
}