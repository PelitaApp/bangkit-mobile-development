package com.dicoding.pelitaapps.retrofit

import com.dicoding.pelitaapps.response.LoginResponse
import com.dicoding.pelitaapps.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("users/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
    @Field("email") email: String,
    @Field("password") password: String
    ): LoginResponse
}