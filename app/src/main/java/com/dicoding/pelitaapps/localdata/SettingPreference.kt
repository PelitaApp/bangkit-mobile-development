package com.dicoding.pelitaapps.localdata

import android.content.Context

internal class SettingPreference
    (context: Context) {
        companion object {
            private const val PREFS_NAME = "user_pref"
            private const val TOKEN = "token"
        }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE )

    fun setUser(token: String) {
        val editor = preferences.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getUser():String? {
        val getToken = preferences.getString(TOKEN, "")
        return getToken
    }

    fun getPrefData(index: String): String?{
        return preferences.getString(index, "")
    }
    fun setPrefData(index: String, value: String){
        val editor = preferences.edit()
        editor.putString(index, value)
        editor.apply()
    }
}