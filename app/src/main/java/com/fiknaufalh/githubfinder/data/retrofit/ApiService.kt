package com.fiknaufalh.githubfinder.data.retrofit

import com.fiknaufalh.githubfinder.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun searchUser(
        @Query("q") id: String
    ): Call<SearchResponse>
}