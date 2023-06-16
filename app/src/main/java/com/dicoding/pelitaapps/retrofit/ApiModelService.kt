package com.dicoding.pelitaapps.retrofit

import com.dicoding.pelitaapps.response.TakePointUserResponse
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiModelService {
    @POST("/predict")
    suspend fun takePointUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Field("point") point: Int,
        @Field("phone") phone: String
    ): TakePointUserResponse
}