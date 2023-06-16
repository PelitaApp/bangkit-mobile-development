package com.dicoding.pelitaapps.response

import com.google.gson.annotations.SerializedName

data class TakePointUserResponse(
    @field:SerializedName("userId")
    val name: Int? = null,

    @field:SerializedName("point")
    val point: Int? = null,

    @field:SerializedName("phone")
    val phone: String? = null
)