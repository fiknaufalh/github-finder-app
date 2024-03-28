package com.fiknaufalh.githubfinder.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fiknaufalh.githubfinder.helpers.ViewModelFactory
import com.fiknaufalh.githubfinder.viewmodels.FavoriteViewModel
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityFavoriteBinding
//    private lateinit var favoriteViewModel: FavoriteViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
//        binding = ActivityFavoriteBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        favoriteViewModel = getViewModel(this)
//
//        favoriteViewModel.favoriteUsers.observe(this) {
//            setFavUserList(it)
//            favoriteViewModel.favoriteUsers.removeObservers(this)
//        }
//
//        val layoutManager = LinearLayoutManager(this)
//        binding.rvFavs.layoutManager = layoutManager
//
//        binding.backTab.setOnClickListener {
//            finish()
//        }
//    }
//
//    private fun getViewModel(activity: FavoriteActivity): FavoriteViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
//    }
//
//    private fun setFavUserList(users: List<FavoriteUser>) {
//        if (users.isNotEmpty()) {
//            val adapter = UsersAdapter(users,
//                onClickCard = {
//                    val intent = Intent(this, DetailActivity::class.java)
//                    intent.putExtra(resources.getString(R.string.stringExtra), it.username)
//                    startActivity(intent)
//                },
//                onClickFav = {
//                    var favoriteUser = FavoriteUser()
//                    favoriteUser.username = it.username!!
//                    favoriteUser.avatarUrl = it.avatarUrl
//                    favoriteUser.htmlUtl = it.htmlUtl!!
//
//                    val isAlreadyFav = favoriteViewModel.isFavorite(favoriteUser)
//                    if (isAlreadyFav) {
//                        favoriteViewModel.removeFavorite(favoriteUser)
//                    } else {
//                        favoriteViewModel.addFavorite(favoriteUser)
//                    }
//                })
//            binding.rvFavorite.adapter = adapter
//            binding.rvFavorite.visibility = View.VISIBLE
//
//            favoriteViewModel.getFavoriteList().observe(this) { favoriteUsers ->
//                adapter.updateFavoriteUsers(favoriteUsers)
//            }
//
//
//        } else {
//            binding.rvFavorite.visibility = View.GONE
//        }
//    }
}