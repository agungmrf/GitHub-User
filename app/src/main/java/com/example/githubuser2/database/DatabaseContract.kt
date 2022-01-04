package com.example.githubuser2.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.githubuser2"
    const val SCHEME = "content"

    internal class UserFavoriteColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_AVATAR = "avatar_url"
            const val COLUMN_NAME = "name"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_TYPE = "type"
            const val COLUMN_COMPANY = "company"
            const val COLUMN_LOCATION = "location"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}