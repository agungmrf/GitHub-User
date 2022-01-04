package com.example.githubuser2.helper

import android.database.Cursor
import android.media.tv.TvContract.Channels.COLUMN_TYPE
import android.provider.BaseColumns._ID
import com.example.githubuser2.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_AVATAR
import com.example.githubuser2.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_COMPANY
import com.example.githubuser2.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_LOCATION
import com.example.githubuser2.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_NAME
import com.example.githubuser2.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_USERNAME
import com.example.githubuser2.model.Favorite

object MappingHelper {

    fun mapCursorToArrayList(userFavoritesCursor: Cursor?): ArrayList<Favorite> {
        val favoriteList = ArrayList<Favorite>()
        userFavoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val avatar = getString(getColumnIndexOrThrow(COLUMN_AVATAR))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val username = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                val type = getString(getColumnIndexOrThrow(COLUMN_TYPE))
                val company = getString(getColumnIndexOrThrow(COLUMN_COMPANY))
                val location = getString(getColumnIndexOrThrow(COLUMN_LOCATION))
                favoriteList.add(
                    Favorite(
                        id,
                        avatar,
                        name,
                        username,
                        type,
                        company,
                        location
                    )
                )
            }
        }
        return favoriteList
    }
}