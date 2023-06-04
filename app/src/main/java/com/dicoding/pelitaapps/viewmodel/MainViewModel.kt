package com.dicoding.pelitaapps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.pelitaapps.remotedata.Result
import com.dicoding.pelitaapps.remotedata.MainRepository
import com.dicoding.pelitaapps.response.RegisterResponse
import kotlinx.coroutines.launch


class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val LiveDataResult = MutableLiveData<Result<RegisterResponse>>()
    private val token = MutableLiveData<String?>()

    fun registerNewUser(
        nama: String,
        password: String,
        email: String
    ): LiveData<Result<RegisterResponse>> {
        viewModelScope.launch {
            val result = mainRepository.registerUser(nama, password, email)
            LiveDataResult.value = result
        }
        return LiveDataResult
    }

    fun loginNewUser(
        email: String, password: String
    ) = mainRepository.loginUser(email, password)


    fun getPreference(
        context: Context
    ) : LiveData<String?> {
        val dataToken = mainRepository.getPreference(context)
        token.value = dataToken
        return token
    }

    fun setPreference(
        token: String, context: Context
    ) = mainRepository.savePreference(token, context)

}