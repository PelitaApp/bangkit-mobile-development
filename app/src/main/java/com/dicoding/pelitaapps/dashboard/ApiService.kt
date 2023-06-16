package com.dicoding.pelitaapps.dashboard
import com.utsman.samplegooglemapsdirection.kotlin.model.DirectionResponses
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("trashes/create")
    fun postCreateTrash(
        @Header("Authorization") authorization: String,
        @Part("userId") userId: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("note") note: RequestBody,
        @Part("address") address: RequestBody
    ): Call<CreateTrashResponse>

    @Multipart
    @POST("predict")
    fun postTrashIdentification(
        @Part file: MultipartBody.Part
    ): Call<TrashPredictionResponse>

    @GET("trashes/user/{userId}")
    fun getTrashFromUser(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: Int
    ): Call<List<TrashFromUserResponseItem>>

    @GET("drivers")
    fun getDrivers(
        @Header("Authorization") authorization: String
    ): Call<List<DriversResponseItem>>

    @FormUrlEncoded
    @PUT("trashes/change/{trashId}")
    fun putChangeTrashStatus(
        @Path("trashId") trashId: Int,
        @Header("Authorization") authorization: String,
        @Field("status") status: String,
        @Field("driverId") driverId: Int
    ): Call<ChangeTrashStatusResponse>

    @GET("locations")
    fun getLocations(
        @Header("Authorization") authorization: String
    ): Call<List<LocationsResponseItem>>

    @FormUrlEncoded
    @POST("locations/upload")
    fun postLocation(
        @Header("Authorization") authorization: String,
        @Field("lat") lat: String,
        @Field("lon") lon: String,
        @Field("driverId") driverId: Int
    ): Call<CreateTrashResponse>

    @FormUrlEncoded
    @PUT("drivers/change/status/{id}")
    fun putChangeDriverStatus(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Field("status") status: String
    ): Call<CreateTrashResponse>

    @GET("trashes/{id}")
    fun getTrashFromTrashId(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<TrashFromUserResponseItem>
    @GET("maps/api/directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionResponses>

    @GET("locations/{id}")
    fun getDriverLocation(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<DriverLocationResponse>

    @GET("drivers/{id}")
    fun getDriverFromId(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<DriversResponseItem>

    @GET("users/{id}")
    fun getUser(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @PUT("users/update/{id}")
    fun putUpdateUser(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
    ): Call<CreateTrashResponse>

    @FormUrlEncoded
    @POST("points/taken/{userId}")
    fun postTakenPoint(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Field("point") name: String,
        @Field("phone") phone: String
    ): Call<CreateTrashResponse>

    @FormUrlEncoded
    @POST("users/login")
    fun postLoginUser(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}