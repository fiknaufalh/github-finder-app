package com.fiknaufalh.githubfinder.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubfinder.BuildConfig
import com.fiknaufalh.githubfinder.data.response.SearchResponse
import com.fiknaufalh.githubfinder.data.retrofit.ApiConfig
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.helpers.Event
import com.fiknaufalh.githubfinder.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : ViewModel() {

    private val mMainRepository: MainRepository = MainRepository(application)

    private val _users = MutableLiveData<SearchResponse>()
    val users: LiveData<SearchResponse> = _users

    private val _favoriteUsers = MutableLiveData<List<FavoriteUser>>()
    val favoriteUsers :LiveData<List<FavoriteUser>> = _favoriteUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<Event<String>>()
    val errorMsg: LiveData<Event<String>> = _errorMsg

    init {
        searchUsers("A")
        mMainRepository.getFavoriteList().observeForever { favoriteList ->
            _favoriteUsers.value = favoriteList
        }
    }

    fun searchUsers(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(q)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()
                }
                else {
                    _errorMsg.value = Event("Server Error, ${response.message()}")
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onFailResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = Event("Error, check your connection!")
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
            }
        })
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

    companion object {
        private const val TAG = "MainViewModel"
    }
}
