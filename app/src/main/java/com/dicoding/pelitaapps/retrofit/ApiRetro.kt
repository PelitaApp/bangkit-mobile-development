package com.dicoding.pelitaapps.retrofit

import android.content.Context
import com.dicoding.pelitaapps.remotedata.MainRepository

object ApiRetro {
    fun provideRepository(context: Context): MainRepository {
        val ApiSer = ApiConfig.getApiService()
        return MainRepository.getInstance(ApiSer)
    }
}