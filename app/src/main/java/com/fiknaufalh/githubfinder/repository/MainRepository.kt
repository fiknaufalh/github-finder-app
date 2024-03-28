package com.fiknaufalh.githubfinder.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.database.FavoriteUserDao
import com.fiknaufalh.githubfinder.database.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository(application: Application) {
    private val mFavoriteDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteDao = db.favUserDao()
    }

    fun getFavoriteList(): LiveData<List<FavoriteUser>> = mFavoriteDao.getFavoriteList()

    fun addFavorite(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteDao.addFavorite(favoriteUser)
        }
    }

    fun removeFavorite(favoriteUser: FavoriteUser) {
        executorService.execute {
            mFavoriteDao.removeFavorite(favoriteUser)
        }
    }
}