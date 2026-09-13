package com.example.dalia2.data

import android.content.Context
import android.system.Os.remove
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.dalia2.data.model.AppMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "dalia_session",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAppMode(mode: AppMode) {
        sharedPreferences.edit().putString("app_mode", mode.name).apply()
    }

    fun getAppMode(): AppMode {
        val modeStr = sharedPreferences.getString("app_mode", AppMode.MENSTRUACAO.name)
        return try {
            AppMode.valueOf(modeStr ?: AppMode.MENSTRUACAO.name)
        } catch (e: Exception) {
            AppMode.MENSTRUACAO
        }
    }
    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString("accessToken", token).apply()
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString("refreshToken", token).apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString("accessToken", null)

    fun getRefreshToken(): String? = sharedPreferences.getString("refreshToken", null)

    fun clearSession() {
        sharedPreferences.edit()
            .remove("accessToken")
            .remove("refreshToken")
            .remove("app_mode")
            .apply()
    }
}