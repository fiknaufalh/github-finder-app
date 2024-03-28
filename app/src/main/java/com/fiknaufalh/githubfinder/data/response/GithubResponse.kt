package com.fiknaufalh.githubfinder.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<UserItem>? = null
)

data class UserItem(

	@field:SerializedName("repos_url")
	val reposUrl: String? = null,

	@field:SerializedName("following_url")
	val followingUrl: String? = null,

	@field:SerializedName("login")
	val login: String? = null,

	@field:SerializedName("followers_url")
	val followersUrl: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
