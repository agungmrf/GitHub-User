package com.example.githubuser2.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser2.R
import com.example.githubuser2.model.Favorite
import kotlinx.android.synthetic.main.item_favorite.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter(private val Favorites: ArrayList<Favorite>) :
    RecyclerView.Adapter<FavoriteAdapter.RecyclerViewHolder>(), Filterable {

    private val TAG = FavoriteAdapter::class.java.simpleName
    private lateinit var onItemClickDetail: OnItemClickCallBack
    private var filterUserFavorite: ArrayList<Favorite> = Favorites

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickDetail = onItemClickCallBack
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserFavoriteData(item: ArrayList<Favorite>) {
        Favorites.clear()
        Favorites.addAll(item)
        notifyDataSetChanged()
        Log.d(TAG, "$item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = filterUserFavorite.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(filterUserFavorite[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userFavorite: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userFavorite.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(riv_item_favorite)
                if (userFavorite.name.isNullOrEmpty() || userFavorite.name == "null") {
                    tv_name_favorite.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_name_detail)
                    }
                } else tv_name_favorite.text = userFavorite.name
                tv_username_favorite.text = userFavorite.username
                if (userFavorite.company.isNullOrEmpty() || userFavorite.company == "null") {
                    tv_company_favorite.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_company_detail)
                    }
                } else tv_company_favorite.text = userFavorite.company
                if (userFavorite.location.isNullOrEmpty() || userFavorite.location == "null") {
                    tv_location_favorite.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_location_detail)
                    }
                } else tv_location_favorite.text = userFavorite.location
                itemView.setOnClickListener { onItemClickDetail.onItemClicked(userFavorite) }
            }
        }
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val itemSearch = constraint.toString()
            filterUserFavorite = if (itemSearch.isEmpty()) Favorites
            else {
                val itemList = ArrayList<Favorite>()
                for (item in Favorites) {
                    val name = item.name
                        ?.toLowerCase(Locale.ROOT)
                        ?.contains(itemSearch.toLowerCase(Locale.ROOT))
                    val userName = item.username
                        ?.toLowerCase(Locale.ROOT)
                        ?.contains(itemSearch.toLowerCase(Locale.ROOT))
                    if (name == true || userName == true) itemList.add(item)
                }
                itemList
            }
            val filterResults = FilterResults()
            filterResults.values = filterUserFavorite
            return filterResults
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterUserFavorite = results?.values as ArrayList<Favorite>
            notifyDataSetChanged()
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: Favorite)
    }
}