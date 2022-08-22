package com.example.appwithtoken

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val pass: String
): Parcelable
