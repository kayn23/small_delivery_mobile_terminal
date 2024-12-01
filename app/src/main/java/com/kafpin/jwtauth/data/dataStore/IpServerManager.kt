package com.kafpin.jwtauth.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IpServerManager @Inject constructor(private  val dataStore: DataStore<Preferences>) {
    private val TAG = "IpServerManager"
    companion object {
        val TOKEN_KEY = stringPreferencesKey("IpServerManager")
    }

    private val _ipFlow = MutableStateFlow<String?>(null)  // Инициализируем с null или значением по умолчанию
    val ipStateFlow: StateFlow<String?> = _ipFlow // Публичный доступ только к StateFlow

    suspend fun saveIpServer(ip: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ip
            _ipFlow.value = ip
        }
    }

    suspend fun clearIpServer() {
        dataStore.edit { preferences ->
            preferences.remove(TokenManager.TOKEN_KEY)
        }
        _ipFlow.value = null
    }

    suspend fun getServerIp(): String? {
        val preferences = dataStore.data.first()
        _ipFlow.value = preferences[TOKEN_KEY]
        return _ipFlow.value
    }

    val ipFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }
}