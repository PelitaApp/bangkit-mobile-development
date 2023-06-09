package com.dicoding.pelitaapps.remotedata

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.pelitaapps.data.LoginUser
import com.dicoding.pelitaapps.data.RegisterUser
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.response.LoginResponse
import com.dicoding.pelitaapps.response.RegisterResponse
import com.dicoding.pelitaapps.retrofit.ApiService
import org.json.JSONObject
import retrofit2.HttpException

class MainRepository private constructor(
    private val apiService: ApiService,
) {

    // Digunakan untuk Register User
    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Result<RegisterResponse> {
        return try {
            val response = apiService.register(username, email, password)
            Result.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()

            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")

            Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getPreference(context: Context): String? {
        val settingPreference = SettingPreference(context)
        return settingPreference.getUser()
    }

    fun savePreference(token: String, context: Context) {
        val settingPreference = SettingPreference(context)
        return settingPreference.setUser(token)
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(apiService: ApiService): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository(apiService)
        }.also { instance = it }
    }
}