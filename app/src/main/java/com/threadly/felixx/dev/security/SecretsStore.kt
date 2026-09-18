package com.threadly.felixx.dev.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecretsStore(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "threadly_secrets",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccountSecret(accountId: String, secret: String) = prefs.edit().putString("account_$accountId", secret).apply()
    fun accountSecret(accountId: String): String? = prefs.getString("account_$accountId", null)
    fun removeAccount(accountId: String) = prefs.edit().remove("account_$accountId").apply()
}
