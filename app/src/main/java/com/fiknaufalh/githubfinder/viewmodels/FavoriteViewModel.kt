package com.fiknaufalh.githubfinder.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.repository.MainRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mMainRepository: MainRepository = MainRepository(application)

    private val _favoriteUsers = MutableLiveData<List<FavoriteUser>>()
    val favoriteUsers :LiveData<List<FavoriteUser>> = _favoriteUsers

    init {
        mMainRepository.getFavoriteList().observeForever { favoriteList ->
            _favoriteUsers.value = favoriteList
        }
    }

    fun isFavorite(favoriteUser: FavoriteUser): Boolean {
        return favoriteUsers.value?.any { it.username == favoriteUser.username } ?: false
    }

    fun getFavoriteList(): LiveData<List<FavoriteUser>> = mMainRepository.getFavoriteList()

    fun addFavorite(favoriteUser: FavoriteUser) {
        mMainRepository.addFavorite(favoriteUser)
    }

    fun removeFavorite(favoriteUser: FavoriteUser) {
        mMainRepository.removeFavorite(favoriteUser)
    }
}