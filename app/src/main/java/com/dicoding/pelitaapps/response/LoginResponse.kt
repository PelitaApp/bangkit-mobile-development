package com.dicoding.pelitaapps.response

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("token")
    val token: String? = null
)

//data class LoginResult(
//    @field:SerializedName("name")
//    val name: String? = null,
//
//    @field:SerializedName("userId")
//    val userId: String? = null,
//
//    @field:SerializedName("token")
//    val token: String? = null
//)