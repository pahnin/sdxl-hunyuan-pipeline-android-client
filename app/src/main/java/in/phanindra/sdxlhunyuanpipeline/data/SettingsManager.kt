package `in`.phanindra.sdxlhunyuanpipeline.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_settings")

class SettingsManager(private val context: Context) {
    companion object {
        private val API_URL_KEY = stringPreferencesKey("api_url")
        private const val DEFAULT_URL = "http://localhost:8000"
    }

    val apiUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[API_URL_KEY] ?: DEFAULT_URL
    }

    suspend fun saveApiUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[API_URL_KEY] = url
        }
    }
}

