package com.dicoding.pelitaapps.dashboard

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("ArticleResponse")
	val articleResponse: List<ArticleResponseItem>
)

data class ArticleResponseItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("type")
	val type: String
)

data class PointResponse(

	@field:SerializedName("total")
	val total: Int
)

