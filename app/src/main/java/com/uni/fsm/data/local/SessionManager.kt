package com.uni.fsm.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uni.fsm.domain.model.User
import kotlinx.coroutines.flow.first


object SessionManager {
    private val KEY_USER_ID = stringPreferencesKey("user_id")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_ROLE = stringPreferencesKey("role")
    private val KEY_USERNAME = stringPreferencesKey("username")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

    suspend fun saveUserSession(context: Context, user: User) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = user.user_id
            prefs[KEY_EMAIL] = user.email
            prefs[KEY_ROLE] = user.role
            prefs[KEY_USERNAME] = user.username
        }
    }

    suspend fun getUserSession(context: Context): User? {
        val prefs = context.dataStore.data.first()

        val userId = prefs[KEY_USER_ID] ?: return null
        val email = prefs[KEY_EMAIL] ?: ""
        val role = prefs[KEY_ROLE] ?: ""
        val username = prefs[KEY_USERNAME] ?: ""

        return User( user_id = userId, email = email, role =  role, username =  username)
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}

