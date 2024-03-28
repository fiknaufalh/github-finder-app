package com.fiknaufalh.githubfinder.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavorite(favoriteUser: FavoriteUser)

    @Delete
    fun removeFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * from favorite_user")
    fun getFavoriteList(): LiveData<List<FavoriteUser>>
}