package com.example.noteapp.utils

import android.text.TextUtils
import android.util.Patterns

object Constants {
   // const val BASE_URL = "https://notes-api-sample.herokuapp.com/"
    const val BASE_URL = "http://staging.php-dev.in:8844/trainingapp/api/"
    const val USER_TOKEN = "user_token"
    const val PREFS_TOKEN_FILE = "prefs_token_file"
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

