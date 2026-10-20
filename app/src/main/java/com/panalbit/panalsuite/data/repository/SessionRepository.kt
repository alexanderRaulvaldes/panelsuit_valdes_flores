package com.panalbit.panalsuite.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionRepository(private val context: Context) {

    private val userIdKey = intPreferencesKey("user_id")

    suspend fun getSavedUserId(): Int? =
        context.dataStore.data.map { it[userIdKey] }.first()

    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { it[userIdKey] = id }
    }

    suspend fun clear() {
        context.dataStore.edit { it.remove(userIdKey) }
    }
}