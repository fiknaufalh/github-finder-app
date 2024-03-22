package com.fiknaufalh.githubfinder.adapters.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.fiknaufalh.githubfinder.data.response.SearchResponse
import com.fiknaufalh.githubfinder.data.response.UserItem
import com.fiknaufalh.githubfinder.data.retrofit.ApiConfig
import com.fiknaufalh.githubfinder.helpers.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _users = MutableLiveData<List<UserItem>>()
    val users: LiveData<List<UserItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<Event<String>>()
    val errorMsg: LiveData<Event<String>> = _errorMsg

    init {
        searchUsers("A")
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
                    _users.value = response.body()?.items!!
                }
                else {
                    _errorMsg.value = Event("Server Error, ${response.message()}")
                    Log.d(TAG, "onResponseFail: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = Event("Error, cek koneksi anda!")
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}