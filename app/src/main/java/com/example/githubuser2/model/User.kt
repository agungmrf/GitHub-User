package com.example.githubuser2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val avatar: String?,
    val username: String?,
    val id: Int?,
    val type: String?
) : Parcelable
