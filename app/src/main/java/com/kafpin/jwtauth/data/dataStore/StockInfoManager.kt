package com.kafpin.jwtauth.data.dataStore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.kafpin.jwtauth.models.stocks.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockInfoManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private val TAG = "StockInfoManager"
    private val gson = Gson()

    companion object {
        val STOCK_INFO_KEY = stringPreferencesKey("stock_info_key")
    }

    suspend fun saveStockInfo(stock: Stock) {
        try {
            val jsonString = gson.toJson(stock)
            dataStore.edit { preferences ->
                preferences[STOCK_INFO_KEY] = jsonString
            }
        } catch (e: Exception) {
            Log.d(TAG, "saveStockInfo exception: ${e.message}")
        }
    }

    suspend fun clearStockInfo() {
        dataStore.edit { preferences ->
            preferences.remove(STOCK_INFO_KEY)
        }
    }

    suspend fun getStockInfo(): Stock? {
        val preferences = dataStore.data.first()
        val jsonString = preferences[STOCK_INFO_KEY]
        return if (jsonString != null) {
            gson.fromJson(jsonString, Stock::class.java)
        } else {
            null
        }
    }

    val stockInfoFlow: Flow<Stock?> = dataStore.data.map { preferences ->
        val jsonString = preferences[STOCK_INFO_KEY]
        if (jsonString == null) {
            null
        } else {
            gson.fromJson(jsonString, Stock::class.java)
        }
    }
}