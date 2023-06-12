package com.dicoding.pelitaapps.dashboard
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("articles")
    fun getArticles(): Call<List<ArticleResponseItem>>

    ///points/total/{userId}
    @GET("points/total/{userId}")
    fun getPoint(
        @Path("userId") userId: Int,
        @Header("Authorization") authorization: String
    ): Call<PointResponse>
}