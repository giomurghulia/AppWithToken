package com.example.appwithtoken

import android.content.Context
import android.content.SharedPreferences

object UserPreference {

     const val TOKEN = "token"

    private val sharedPreference: SharedPreferences? by lazy {
        App.context?.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    }

    fun saveString(token: String) {
        sharedPreference?.edit()?.putString(TOKEN, token)?.apply()
    }

    fun getString(key: String) = sharedPreference?.getString(key, null)
}