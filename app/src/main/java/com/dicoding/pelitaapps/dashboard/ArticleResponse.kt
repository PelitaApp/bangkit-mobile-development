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

data class CreateTrashResponse(

	@field:SerializedName("message")
	val message: String
)

data class ChangeTrashStatusResponse(

	@field:SerializedName("message")
	val message: String
)

data class TrashFromUserResponse(

	@field:SerializedName("TrashFromUserResponse")
	val trashFromUserResponse: List<TrashFromUserResponseItem>
)

data class TrashFromUserResponseItem(

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("driver_id")
	val driverId: Int,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("status")
	val status: String
)

data class DriversResponse(

	@field:SerializedName("DriversResponse")
	val driversResponse: List<DriversResponseItem>
)

data class DriversResponseItem(

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("status")
	val status: String
)

data class LocationsResponse(

	@field:SerializedName("LocationsResponse")
	val locationsResponse: List<LocationsResponseItem>
)

data class LocationsResponseItem(

	@field:SerializedName("driver_id")
	val driverId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("lat")
	val lat: String
)

data class DriverLocationResponse(

	@field:SerializedName("driver_id")
	val driverId: Int,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("lat")
	val lat: String
)

data class TrashPredictionResponse(

	@field:SerializedName("prediction")
	val prediction: String
)

data class UserResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phone")
	val phone: Any? = null,

	@field:SerializedName("name")
	val name: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class LoginResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("token")
	val token: String
)
