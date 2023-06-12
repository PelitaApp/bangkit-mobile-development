package com.dicoding.pelitaapps.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    private val _listArticles = MutableLiveData<List<ArticleResponseItem>>()
    val listArticles: LiveData<List<ArticleResponseItem>> = _listArticles
    private val _totalpoint = MutableLiveData<PointResponse>()
    val totalpoint: LiveData<PointResponse> = _totalpoint
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    init {
        _isError.value = null
    }

    fun getArticles() {
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticles()
        client.enqueue(object : Callback<List<ArticleResponseItem>> {
            override fun onResponse(
                call: Call<List<ArticleResponseItem>>,
                response: Response<List<ArticleResponseItem>>
            ) {
                Log.d("eaa","ea1")
                _isLoading.value = false
                Log.d("eaa","ea2")
                if (response.isSuccessful) {
                    Log.d("eaa","ea3")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listArticles.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ArticleResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun getPoint(userId: Int, token: String) {
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getPoint(userId, token)
        client.enqueue(object : Callback<PointResponse> {
            override fun onResponse(
                call: Call<PointResponse>,
                response: Response<PointResponse>
            ) {
                Log.d("eaa","ea1")
                _isLoading.value = false
                Log.d("eaa","ea2")
                if (response.isSuccessful) {
                    Log.d("eaa","ea3")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _totalpoint.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PointResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    companion object{
        private const val TAG = "DashboardViewModel"
    }
}