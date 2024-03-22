package com.fiknaufalh.githubfinder.data.retrofit

import com.fiknaufalh.githubfinder.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUser(
        @Query("q") id: String
    ): Call<SearchResponse>
}