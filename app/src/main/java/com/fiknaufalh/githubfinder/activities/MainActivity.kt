package com.fiknaufalh.githubfinder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.adapters.UserAdapter
import com.fiknaufalh.githubfinder.adapters.viewmodels.MainViewModel
import com.fiknaufalh.githubfinder.data.response.SearchResponse
import com.fiknaufalh.githubfinder.data.response.UserItem
import com.fiknaufalh.githubfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        val mainViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.users.observe(this) { users ->
            setUserListData(users)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserListData(users: List<UserItem>) {
        val adapter = UserAdapter(users)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}