package com.dicoding.pelitaapps.retrofit

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResp

    @FormUrlEncoded
    @POST("login")
    suspend fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResp
}