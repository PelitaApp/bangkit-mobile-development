package com.dicoding.pelitaapps.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.pelitaapps.reduceFileImage
import com.utsman.samplegooglemapsdirection.kotlin.model.DirectionResponses
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DashboardViewModel : ViewModel() {
    private val _listArticles = MutableLiveData<List<ArticleResponseItem>>()
    val listArticles: LiveData<List<ArticleResponseItem>> = _listArticles
    private val _totalpoint = MutableLiveData<PointResponse>()
    val totalpoint: LiveData<PointResponse> = _totalpoint
    private val _createTrashResult = MutableLiveData<CreateTrashResponse>()
    val createTrashResult: LiveData<CreateTrashResponse> = _createTrashResult
    private val _trashFromUserRes = MutableLiveData<List<TrashFromUserResponseItem>>()
    val trashFromUserRes: LiveData<List<TrashFromUserResponseItem>> = _trashFromUserRes
    private val _driversRes = MutableLiveData<List<DriversResponseItem>>()
    val driversRes: LiveData<List<DriversResponseItem>> = _driversRes
    private val _locationsRes = MutableLiveData<List<LocationsResponseItem>>()
    val locationsRes: LiveData<List<LocationsResponseItem>> = _locationsRes
    private val _changeTrashStatusRes = MutableLiveData<ChangeTrashStatusResponse>()
    val changeTrashStatusRes: LiveData<ChangeTrashStatusResponse> = _changeTrashStatusRes
    private val _postLocationRes = MutableLiveData<CreateTrashResponse>()
    val postLocationRes: LiveData<CreateTrashResponse> = _postLocationRes
    private val _putChangeDriverStatusRes = MutableLiveData<CreateTrashResponse>()
    val putChangeDriverStatusRes: LiveData<CreateTrashResponse> = _putChangeDriverStatusRes
    private val _getTrashFromTrashIdRes = MutableLiveData<TrashFromUserResponseItem>()
    val getTrashFromTrashIdRes: LiveData<TrashFromUserResponseItem> = _getTrashFromTrashIdRes
    private val _getDirectionRes = MutableLiveData<DirectionResponses>()
    val getDirectionRes: LiveData<DirectionResponses> = _getDirectionRes
    private val _getDriverLocationRes = MutableLiveData<DriverLocationResponse>()
    val getDriverLocationRes: LiveData<DriverLocationResponse> = _getDriverLocationRes
    private val _getDriverFromIdRes = MutableLiveData<DriversResponseItem>()
    val getDriverFromIdRes: LiveData<DriversResponseItem> = _getDriverFromIdRes
    private val _postTrashIdentificationRes = MutableLiveData<TrashPredictionResponse>()
    val postTrashIdentificationRes: LiveData<TrashPredictionResponse> = _postTrashIdentificationRes
    private val _getUserRes = MutableLiveData<UserResponse>()
    val getUserRes: LiveData<UserResponse> = _getUserRes
    private val _putUpdateUserRes = MutableLiveData<CreateTrashResponse>()
    val putUpdateUserRes: LiveData<CreateTrashResponse> = _putUpdateUserRes
    private val _postTakenPointRes = MutableLiveData<CreateTrashResponse>()
    val postTakenPointRes: LiveData<CreateTrashResponse> = _postTakenPointRes
    private val _postLoginUserRes = MutableLiveData<LoginResponse>()
    val postLoginUserRes: LiveData<LoginResponse> = _postLoginUserRes
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
                
                _isLoading.value = false
                if (response.isSuccessful) {
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
                
                _isLoading.value = false
                if (response.isSuccessful) {
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

    fun postCreateTrash(token: String, userId: Int, image: File, type: String, weight: String, note: String, address: String){
        _isLoading.value = true
        val vUserId = userId.toString().toRequestBody("text/plain".toMediaType())
        val vImage: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            reduceFileImage(image).name,
            image.asRequestBody("image/jpeg".toMediaType())
        )
        val vType = type.toRequestBody("text/plain".toMediaType())
        val vWeight = weight.toRequestBody("text/plain".toMediaType())
        val vNote = note.toRequestBody("text/plain".toMediaType())
        val vAddress = address.toRequestBody("text/plain".toMediaType())
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().postCreateTrash(token, vUserId, vImage, vType, vWeight, vNote, vAddress)
        client.enqueue(object : Callback<CreateTrashResponse> {
            override fun onResponse(
                call: Call<CreateTrashResponse>,
                response: Response<CreateTrashResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _createTrashResult.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateTrashResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun postTrashIdentification(file: File){
        _isLoading.value = true
        val vImage: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            reduceFileImage(file).name,
            file.asRequestBody("image/jpeg".toMediaType())
        )
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService(isPrediction = true).postTrashIdentification(vImage)
        client.enqueue(object : Callback<TrashPredictionResponse> {
            override fun onResponse(
                call: Call<TrashPredictionResponse>,
                response: Response<TrashPredictionResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _postTrashIdentificationRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TrashPredictionResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "Error: ${t.message}"
            }
        })
    }

    fun getTrashFromUser(token: String, userId: Int){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTrashFromUser(token, userId)
        client.enqueue(object : Callback<List<TrashFromUserResponseItem>> {
            override fun onResponse(
                call: Call<List<TrashFromUserResponseItem>>,
                response: Response<List<TrashFromUserResponseItem>>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _trashFromUserRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<TrashFromUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun getDrivers(token: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDrivers(token)
        client.enqueue(object : Callback<List<DriversResponseItem>> {
            override fun onResponse(
                call: Call<List<DriversResponseItem>>,
                response: Response<List<DriversResponseItem>>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _driversRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<DriversResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun putChangeTrashStatus(token: String, trashId: Int, status: String, driverId: Int){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().putChangeTrashStatus(trashId, token, status, driverId)
        client.enqueue(object : Callback<ChangeTrashStatusResponse> {
            override fun onResponse(
                call: Call<ChangeTrashStatusResponse>,
                response: Response<ChangeTrashStatusResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _changeTrashStatusRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ChangeTrashStatusResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun getLocations(token: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getLocations(token)
        client.enqueue(object : Callback<List<LocationsResponseItem>> {
            override fun onResponse(
                call: Call<List<LocationsResponseItem>>,
                response: Response<List<LocationsResponseItem>>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _locationsRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<LocationsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun postLocation(token: String, lat: String, lon: String, driverId: Int){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLocation(token, lat, lon, driverId)
        client.enqueue(object : Callback<CreateTrashResponse> {
            override fun onResponse(
                call: Call<CreateTrashResponse>,
                response: Response<CreateTrashResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _postLocationRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateTrashResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun putChangeDriverStatus(token: String, id: String, status: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().putChangeDriverStatus(token, id, status)
        client.enqueue(object : Callback<CreateTrashResponse> {
            override fun onResponse(
                call: Call<CreateTrashResponse>,
                response: Response<CreateTrashResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _putChangeDriverStatusRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateTrashResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun getTrashFromTrashId(token: String, id: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTrashFromTrashId(token,id)
        client.enqueue(object : Callback<TrashFromUserResponseItem> {
            override fun onResponse(
                call: Call<TrashFromUserResponseItem>,
                response: Response<TrashFromUserResponseItem>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _getTrashFromTrashIdRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<TrashFromUserResponseItem>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun getDirection(origin: String, destination: String, key: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService(true).getDirection(origin, destination, key)
        client.enqueue(object : Callback<DirectionResponses> {
            override fun onResponse(
                call: Call<DirectionResponses>,
                response: Response<DirectionResponses>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _getDirectionRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun getDriverLocation(token: String, id: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDriverLocation(token, id)
        client.enqueue(object : Callback<DriverLocationResponse> {
            override fun onResponse(
                call: Call<DriverLocationResponse>,
                response: Response<DriverLocationResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _getDriverLocationRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DriverLocationResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun getDriverFromId(token: String, id: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDriverFromId(token, id)
        client.enqueue(object : Callback<DriversResponseItem> {
            override fun onResponse(
                call: Call<DriversResponseItem>,
                response: Response<DriversResponseItem>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _getDriverFromIdRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DriversResponseItem>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }
    fun getUser(token: String, id: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(token, id)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _getUserRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun putUpdateUser(token: String, id: String, name: String, email: String, phone: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().putUpdateUser(token, id, name, email, phone)
        client.enqueue(object : Callback<CreateTrashResponse> {
            override fun onResponse(
                call: Call<CreateTrashResponse>,
                response: Response<CreateTrashResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _putUpdateUserRes.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateTrashResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun postTakenPoint(token: String, userId: String, point: String, phone: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().postTakenPoint(token, userId, point, phone)
        client.enqueue(object : Callback<CreateTrashResponse> {
            override fun onResponse(
                call: Call<CreateTrashResponse>,
                response: Response<CreateTrashResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _postTakenPointRes.value = responseBody!!
                    }
                } else {
                    _postTakenPointRes.value = CreateTrashResponse(
                        "gagal"
                    )
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<CreateTrashResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailured: ${t.message}")
                _isError.value = "onFailure: ${t.message}"
            }
        })
    }

    fun postLoginUser(token: String, email: String, password: String){
        _isError.value = null
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLoginUser(token, email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _postLoginUserRes.value = responseBody!!
                    }
                } else {
                    val responseBody = response.body()
                    _postLoginUserRes.value = LoginResponse(
                        0,
                        "gagal",
                        ""
                    )
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _postLoginUserRes.value = LoginResponse(
                    0,
                    "gagal",
                    ""
                )
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