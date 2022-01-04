package com.example.githubuser2.viewmodel

import android.content.Context
import android.media.tv.TvContract.Channels.CONTENT_URI
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser2.helper.MappingHelper
import com.example.githubuser2.model.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }

    private val listUserFavorites = MutableLiveData<ArrayList<Favorite>>()

    fun setUserFavorite(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val deferredUserFavorites = async(Dispatchers.IO) {
                    val cursor = context.contentResolver.query(
                        CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                    )
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val userFavorites = deferredUserFavorites.await()
                Log.d(TAG, "$userFavorites")
                listUserFavorites.postValue(userFavorites)
                Log.d(TAG, "$listUserFavorites")
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                Toast.makeText(context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getUserFavorite(): LiveData<ArrayList<Favorite>> = listUserFavorites
}