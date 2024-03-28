package com.fiknaufalh.githubfinder.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubfinder.BuildConfig
import com.fiknaufalh.githubfinder.data.response.UserDetailResponse
import com.fiknaufalh.githubfinder.data.response.UserItem
import com.fiknaufalh.githubfinder.data.retrofit.ApiConfig
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.helpers.Event
import com.fiknaufalh.githubfinder.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val mMainRepository: MainRepository = MainRepository(application)

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _followList = MutableLiveData<List<UserItem>>()
    val followList: LiveData<List<UserItem>> = _followList

    private val _favoriteUsers = MutableLiveData<List<FavoriteUser>>()
    val favoriteUsers :LiveData<List<FavoriteUser>> = _favoriteUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<Event<String>>()
    val errorMsg: LiveData<Event<String>> = _errorMsg

    init {
        mMainRepository.getFavoriteList().observeForever { favoriteList ->
            _favoriteUsers.value = favoriteList
        }
    }

    fun getDetail(q: String) {
        if (userDetail.value?.login != null) return

        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(q)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    _errorMsg.value = Event("Server Error, ${response.message()}")
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onFailResponse: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = Event("Error, check your connection!")
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollow(type: String, username: String) {
        if(followList.value != null) return

        _isLoading.value = true
        val client = if (type == "followers") {
            ApiConfig.getApiService().getFollowers(username)
        } else {
            ApiConfig.getApiService().getFollowing(username)
        }

        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followList.value = response.body()
                } else {
                    _errorMsg.value = Event("Server Error, ${response.message()}")
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "onFailResponse: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = Event("Error, check your connection!")
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun isFavorite(username: String): Boolean {
        return favoriteUsers.value?.any { it.username == username } ?: false
    }

    fun getFavoriteList(): LiveData<List<FavoriteUser>> = mMainRepository.getFavoriteList()

    fun addFavorite(favoriteUser: FavoriteUser) {
        mMainRepository.addFavorite(favoriteUser)
    }

    fun removeFavorite(favoriteUser: FavoriteUser) {
        mMainRepository.removeFavorite(favoriteUser)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}