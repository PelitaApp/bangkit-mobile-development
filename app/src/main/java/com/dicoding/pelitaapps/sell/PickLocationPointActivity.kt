package com.dicoding.pelitaapps.sell

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityPickLocationPointBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class PickLocationPointActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPickLocationPointBinding
    private lateinit var mMap: GoogleMap
    private lateinit var locationMarker: Marker
    private lateinit var searchEditText: EditText
    private var namedLoc: String = ""
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickLocationPointBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnLanjut.isEnabled = false
        binding.btnLanjut.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_NAMED_LOC, namedLoc)
            resultIntent.putExtra(EXTRA_EXACT_LOC_LAT, locationMarker.position.latitude.toString())
            resultIntent.putExtra(EXTRA_EXACT_LOC_LON, locationMarker.position.longitude.toString())
            setResult(RESULT_CODE,resultIntent)
            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraMoveListener {
            locationMarker.position = mMap.cameraPosition.target
        }
        mMap.setOnCameraIdleListener {
            binding.locationActive.text = getAddressName(locationMarker.position.latitude,locationMarker.position.longitude)
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true


        getMyLocation()

        mMap.setPadding(0,0,0,40)
        val initialLatLng = mMap.cameraPosition.target
        locationMarker = mMap.addMarker(MarkerOptions().position(initialLatLng).title("Selected Location"))!!
        locationMarker.position = mMap.cameraPosition.target
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val latLng = LatLng(location.latitude, location.longitude)
                    boundsBuilder.include(latLng)
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@PickLocationPointActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addressName != null) {
            namedLoc = addressName
            binding.btnLanjut.isEnabled = true
        }
        return addressName
    }

    companion object {
        const val EXTRA_NAMED_LOC = "EXTRA_NAMED_LOC"
        const val EXTRA_EXACT_LOC_LAT = "EXTRA_EXACT_LOC_LAT"
        const val EXTRA_EXACT_LOC_LON = "EXTRA_EXACT_LOC_LON"
        const val RESULT_CODE = 110
    }
}