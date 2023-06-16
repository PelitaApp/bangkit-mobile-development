package com.dicoding.pelitaapps.dashboard

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(isMap: Boolean = false, isPrediction: Boolean = false): ApiService {
            var vBaseUrl: String = ""
            if(isMap){
                vBaseUrl = "https://maps.googleapis.com"
            }else if(isPrediction){
                vBaseUrl = "https://modelapi-t5wb5y3n3a-et.a.run.app/"
            }else{
                vBaseUrl = "https://pelita-app.et.r.appspot.com/"
            }
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(vBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}