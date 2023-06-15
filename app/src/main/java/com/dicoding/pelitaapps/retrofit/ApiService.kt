package com.dicoding.pelitaapps.retrofit

import com.dicoding.pelitaapps.data.LoginUser
import com.dicoding.pelitaapps.data.RegisterUser
import com.dicoding.pelitaapps.response.LoginResponse
import com.dicoding.pelitaapps.response.RegisterResponse
import com.dicoding.pelitaapps.response.TakePointUserResponse
import com.dicoding.pelitaapps.response.UserResponse
import com.dicoding.pelitaapps.response.UserUpdateResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("users/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): UserResponse

    @PUT("users/update/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): UserUpdateResponse

    @POST("/points/taken/{userId}")
    suspend fun takePointUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Field("point") point: Int,
        @Field("phone") phone: String
    ): TakePointUserResponse


}