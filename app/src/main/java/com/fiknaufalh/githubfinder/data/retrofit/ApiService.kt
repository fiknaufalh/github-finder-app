package com.fiknaufalh.githubfinder.data.retrofit

import com.fiknaufalh.githubfinder.data.response.UserDetailResponse
import com.fiknaufalh.githubfinder.data.response.SearchResponse
import com.fiknaufalh.githubfinder.data.response.UserItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun searchUser(
        @Query("q") id: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>

}