package com.fiknaufalh.githubfinder.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.adapters.UserAdapter
import com.fiknaufalh.githubfinder.viewmodels.MainViewModel
import com.fiknaufalh.githubfinder.data.response.SearchResponse
import com.fiknaufalh.githubfinder.databinding.ActivityMainBinding
import com.fiknaufalh.githubfinder.helpers.Event
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var isSearched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchUser
        initializeSearchView(searchManager, searchView)

        mainViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.users.observe(this) {
            users -> setUserListData(users)
        }

        mainViewModel.isLoading.observe(this) {
            isLoading -> showLoading(isLoading)
        }

        mainViewModel.errorMsg.observe(this) {
            msg -> setErrorMessage(msg)
        }
    }

    private fun setUserListData(users: SearchResponse) {
        val adapter = UserAdapter(
            users.items!!,
            onClickCard = {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(resources.getString(R.string.passing_query), it.login)
                startActivity(intent)
            })

        binding.rvUsers.adapter = adapter
        if (isSearched) {
            binding.tvTotalResult.text = resources.getString(R.string.search_result_text, users.totalCount, users.items.size)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setErrorMessage(msg: Event<String>) {
        msg.getContentIfNotHandled()?.let {
            val snackBar = Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_SHORT
            )
            snackBar.anchorView = binding.botView
            snackBar.show()
        }
    }

    private fun initializeSearchView(searchManager: SearchManager, searchView: SearchView) {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_placeholder)
        searchView.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            searchView.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(q: String?): Boolean {
                if (q?.length!! >= 3) {
                    binding.rvUsers.visibility = RecyclerView.VISIBLE
                    mainViewModel.searchUsers(q.toString())
                    isSearched = true
                } else if (q.isEmpty()) {
                    binding.rvUsers.visibility = RecyclerView.VISIBLE
                    binding.tvTotalResult.text = resources.getString(R.string.search_me_text)
                    mainViewModel.searchUsers("a")
                    isSearched = false
                } else {
                    binding.rvUsers.visibility = RecyclerView.GONE
                    binding.tvTotalResult.text = resources.getString(R.string.search_me_text)
                    isSearched = false
                }

                return false
            }
        })
    }
}