package com.kafpin.jwtauth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class Role(val id: String, val title: String) {
    Client("1", "client"),
    Admin("2", "admin");

    companion object {
        fun findById(id: String): Role? {
            return values().find { it.id == id }
        }
    }
}

@Singleton
class RoleManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val ROLE_KEY = stringPreferencesKey("role_key")
    }

    suspend fun saveRole(role: String) {
        dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun clearRole() {
        dataStore.edit { preferences ->
            preferences.remove(ROLE_KEY)
        }
    }

    suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[ROLE_KEY]
    }

    val roleFlow: Flow<Role?> = dataStore.data.map { preferences ->
        val res = preferences[ROLE_KEY]
        if (res == null) {
            null
        } else {
            Role.findById(res)
        }
    }
}