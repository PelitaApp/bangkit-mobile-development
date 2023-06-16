package com.dicoding.pelitaapps.response

import com.google.gson.annotations.SerializedName

data class UserUpdateResponse(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("phone")
    val phone: Int? = null

)