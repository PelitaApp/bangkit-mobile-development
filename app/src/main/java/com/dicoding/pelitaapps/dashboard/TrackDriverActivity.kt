package com.dicoding.pelitaapps.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityTrackDriverBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.utsman.samplegooglemapsdirection.kotlin.model.DirectionResponses
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.Executors


class TrackDriverActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrackDriverBinding
    private lateinit var map: GoogleMap
    private lateinit var fkip: LatLng
    private lateinit var driver: LatLng
    private lateinit var locationMarker: Marker
    private lateinit var currentLatLng: LatLng
    private var vMarkerCurrentLatLng: Marker? = null
    private var vMarkerDriver: Marker? = null
    private var pPolyline: com.google.android.gms.maps.model.Polyline? = null
    private lateinit var trashLocationLatLon: Map<String, String>
    private val boundsBuilder = LatLngBounds.Builder()
    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private var isActivityRunning = true
    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        trashLocationLatLon = locSplit(getPrefData("trashLocationLatLon"))
        executor.execute {
            try {
                while(isActivityRunning){
                    Thread.sleep(20000)
                    handler.post {
                        dashboardViewModel.getDriverLocation(
                            "Bearer ".plus(getPrefData("token")),
                            getPrefData("currentLocationId")
                        )
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnCallDriver.setOnClickListener{
            requestCallPhonePermission()
            val phoneNumber = getPrefData("currentDriverPhone") // Replace with the desired phone number


            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        isActivityRunning = false
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        if(trashLocationLatLon != null){
            currentLatLng = LatLng(trashLocationLatLon["lat"]!!.toDouble(), trashLocationLatLon["lon"]!!.toDouble())
        }
        val currentDriverLocationLatLon = locSplit(getPrefData("currentDriverLocationLatLon"))
        driver = LatLng(currentDriverLocationLatLon["lat"]!!.toDouble(), currentDriverLocationLatLon["lon"]!!.toDouble())
        getMyLocation()

        val markerCurrentLatLng = MarkerOptions()
            .position(currentLatLng)
            .title("Current location")
            .icon(vectorToBitmap(R.drawable.ic_location, Color.parseColor("#2E7D32")))
        vMarkerCurrentLatLng = map.addMarker(markerCurrentLatLng)

        val markerDriver = MarkerOptions()
            .position(driver)
            .title("Driver location")
            .icon(vectorToBitmap(R.drawable.ic_motorcycle, Color.parseColor("#000000")))
        vMarkerDriver = map.addMarker(markerDriver)

        val currentLatLngString = currentLatLng.latitude.toString() + "," + currentLatLng.longitude.toString()
        val toDriver = driver.latitude.toString() + "," + driver.longitude.toString()
        dashboardViewModel.getDirection(toDriver, currentLatLngString, getString(R.string.google_maps_api_key))
        dashboardViewModel.getDirectionRes.observe(this){
            drawPolyline(it)
            binding.tvDriverName.text = getPrefData("currentDriverName")
            binding.tvDriverPhone.text = getPrefData("currentDriverPhone")
            binding.tvOtwEta.text = getString(R.string.otw_eta,toTime((it.routes?.get(0)?.legs?.get(0)?.duration?.value!!)))
            binding.tvCurrentTime.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
                Calendar.getInstance().time)
        }
        dashboardViewModel.getDriverLocationRes.observe(this){
            dashboardViewModel.getDirection("${it.lat},${it.lon}", currentLatLngString, getString(R.string.google_maps_api_key))
            updateDriverMarker(it.lat.toDouble(), it.lon.toDouble())
        }
        boundsBuilder.include(currentLatLng)
        boundsBuilder.include(driver)
        val bounds: LatLngBounds = boundsBuilder.build()
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun requestCallPhonePermission() {
        val permission = Manifest.permission.CALL_PHONE
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), CALL_PHONE_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    private fun updateDriverMarker(lat: Double, lon: Double){
        vMarkerDriver?.position = LatLng(lat,lon)
    }

    private fun drawPolyline(response: DirectionResponses) {
        if(pPolyline!=null){
            pPolyline?.remove()
        }
        val shape = response.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)
        pPolyline = map.addPolyline(polyline)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                //getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@TrackDriverActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }
    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
    fun locSplit(alamat: String): Map<String, String> {
        return mapOf(
            "lat" to alamat.split(",")[0],
            "lon" to alamat.split(",")[1]
        )
    }
    fun toTime(seconds: Int): String{
        return "${seconds/3600}h ${(seconds-(3600*(seconds/3600)))/60}m ${seconds - (((seconds/3600)*3600)+(((seconds-(3600*(seconds/3600)))/60)*60))}s"
    }
    private fun getPrefData(index: String): String{
        return SettingPreference(this@TrackDriverActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@TrackDriverActivity).setPrefData(index,value).toString()
    }

    companion object {
        const val EXTRA_NAMED_LOC = "EXTRA_NAMED_LOC"
        const val EXTRA_EXACT_LOC_LAT = "EXTRA_EXACT_LOC_LAT"
        const val EXTRA_EXACT_LOC_LON = "EXTRA_EXACT_LOC_LON"
        const val RESULT_CODE = 110
        private const val CALL_PHONE_PERMISSION_REQUEST_CODE = 1
    }
}