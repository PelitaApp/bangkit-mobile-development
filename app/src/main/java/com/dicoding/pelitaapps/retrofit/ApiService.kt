package com.dicoding.pelitaapps.retrofit

import com.dicoding.pelitaapps.data.LoginUser
import com.dicoding.pelitaapps.data.RegisterUser
import com.dicoding.pelitaapps.response.LoginResponse
import com.dicoding.pelitaapps.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Body requestBody: RegisterUser
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login (
        @Body requestBody: LoginUser
    ) : LoginResponse
}