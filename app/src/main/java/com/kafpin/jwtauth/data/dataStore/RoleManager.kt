package com.kafpin.jwtauth.data.dataStore

import android.util.Log
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
    Admin("2", "admin"),
    Courier("3", "courier");

    companion object {
        fun findById(id: String): Role? {
            return values().find { it.id == id }
        }
        fun findByName(name: String): Role? {
            return values().find {it.title == name}
        }
    }
}

@Singleton
class RoleManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val TAG = "raleManager"
    companion object {
        val ROLE_KEY = stringPreferencesKey("role_key")
        val USER_ID_KEY = stringPreferencesKey("user_info_key")
    }

    suspend fun saveRole(role: String) {
        try {
            dataStore.edit { preferences ->
                preferences[ROLE_KEY] = role
            }
        } catch (e: Exception) {
            Log.d(TAG, "saveRole: ${e.message}")
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
            Role.findByName(res)
        }
    }

    suspend fun saveUserId(id: Int) {
        try {
            dataStore.edit { preferences ->
                preferences[USER_ID_KEY] = id.toString()
            }
        } catch(e: Exception) {
            Log.d(TAG, "saveUserId: ${e.message}")
        }
    }

    val userIdFlow: Flow<Int?> = dataStore.data.map { preferences ->
         val res = preferences[USER_ID_KEY]
        if (res == null) {
            null
        } else {
            res.toInt()
        }
    }
}