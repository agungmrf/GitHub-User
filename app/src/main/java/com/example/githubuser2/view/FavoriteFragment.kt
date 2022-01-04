package com.example.githubuser2.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser2.R
import com.example.githubuser2.model.Favorite
import com.example.githubuser2.view.adapter.FavoriteAdapter
import com.example.githubuser2.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var userFavoriteAdapter: FavoriteAdapter
    private lateinit var userFavoriteViewModel: FavoriteViewModel
    private lateinit var searchView: SearchView
    private var list = ArrayList<Favorite>()

    companion object {
        private val TAG = FavoriteFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorite, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        setHasOptionsMenu(true)
        showRecyclerView()
        userFavoriteViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FavoriteViewModel::class.java).apply {
                tv_favorite_result.text = resources.getString(R.string.load_database)
                showLoadingUserFavorites(true)
                setUserFavorite(requireContext())
                getUserFavorite().observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        showUserFavoriteItems(it)
                        Log.d(TAG, "$it")
                        showLoadingUserFavorites(false)
                    }
                })
            }
    }

    private fun setActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.app_title_favorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.app_bar_search)
        searchView = (searchItem?.actionView as SearchView).apply {
            queryHint = resources.getString(R.string.search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                @SuppressLint("SetTextI18n")
                override fun onQueryTextChange(newText: String?): Boolean {
                    userFavoriteAdapter.filter.filter(newText)
                    if (newText.isNullOrEmpty()) {
                        if (list.size >= 1) tv_favorite_result.text =
                            "${resources.getString(R.string.app_title_favorite)}: ${list.size}"
                        else if (list.size == 0) tv_favorite_result.text =
                            getString(R.string.no_favorite_added)
                    } else tv_favorite_result.text =
                        getString(R.string.user_search_result)
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onNavigateUp(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun onNavigateUp(itemId: Int) {
        if (itemId == android.R.id.home) {
            activity?.onBackPressed()
            closeKeyboard()
        }
    }

    private fun showRecyclerView() {
        rv_favorite_user.setHasFixedSize(true)
        rv_favorite_user.layoutManager = LinearLayoutManager(activity)
        userFavoriteAdapter = FavoriteAdapter(list)
        rv_favorite_user.adapter = userFavoriteAdapter
        userFavoriteAdapter.notifyDataSetChanged()
        userFavoriteAdapter.setOnItemClickCallback(object :
            FavoriteAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: Favorite) = setSelectedUser(data)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showUserFavoriteItems(userFavoriteItems: ArrayList<Favorite>) {
        userFavoriteAdapter.setUserFavoriteData(userFavoriteItems)
        if (userFavoriteItems.size >= 1) tv_favorite_result.text =
            "${resources.getString(R.string.app_title_favorite)}: ${userFavoriteItems.size}"
        else if (userFavoriteItems.size == 0) tv_favorite_result.text =
            getString(R.string.no_favorite_added)
    }

    private fun setSelectedUser(data: Favorite) {
        val mBundle = Bundle().apply {
            putString(DetailUserFragment.EXTRA_AVATAR, data.avatar)
            putString(DetailUserFragment.EXTRA_USERNAME, data.username)
            putString(DetailUserFragment.EXTRA_TYPE, data.type)
        }
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_favoriteFragment_to_detailUserFragment, mBundle)
        closeKeyboard()
    }

    private fun showLoadingUserFavorites(state: Boolean) {
        if (state) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun closeKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val mInputMethodManager: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}