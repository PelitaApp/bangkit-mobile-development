package com.dicoding.pelitaapps.sell

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.createCustomTempFile
import com.dicoding.pelitaapps.dashboard.DashboardViewModel
import com.dicoding.pelitaapps.dashboard.DriversResponseItem
import com.dicoding.pelitaapps.dashboard.LocationsResponseItem
import com.dicoding.pelitaapps.dashboard.TrashFromUserResponseItem
import com.dicoding.pelitaapps.databinding.ActivitySellBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.result.ResultActivity
import java.io.File

class SellActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellBinding
    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private var namedLoc: String? = ""
    private var exactLocLat: String? = ""
    private var exactLocLon: String? = ""
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == PickLocationPointActivity.RESULT_CODE && result.data != null){
            namedLoc = result.data?.getStringExtra(PickLocationPointActivity.EXTRA_NAMED_LOC)
            exactLocLat = result.data?.getStringExtra(PickLocationPointActivity.EXTRA_EXACT_LOC_LAT)
            exactLocLon = result.data?.getStringExtra(PickLocationPointActivity.EXTRA_EXACT_LOC_LON)
            binding.tvAlamat.text = getString(R.string.address,namedLoc)
            Glide.with(this@SellActivity)
                .load("https://maps.googleapis.com/maps/api/staticmap?size=300x150&maptype=roadmap\\&zoom=15&markers=size:mid%7Ccolor:red%7C$exactLocLat,$exactLocLon&key="+getString(R.string.google_maps_api_key))
                .into(binding.ivMap)
            binding.klikDisini.visibility = View.INVISIBLE
        } else if(result.data == null){
            getString(R.string.address,"-")
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvAlamat.text = getString(R.string.address,"-")
        supportActionBar?.hide()

        binding.pbSell.visibility = View.GONE

        if(getPrefData("currentTrashId").isNotEmpty()){
            dashboardViewModel.getTrashFromTrashId(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("currentTrashId")
            )
        }
        dashboardViewModel.getTrashFromTrashIdRes.observe(this){
            if(it.status.contains("otw")){
                //toast tell user that "There is already trash in the pick-up process"
                Toast.makeText(this, getString(R.string.trash_already), Toast.LENGTH_SHORT).show()
                finish()
            }else{
                setPrefData("currentDriverId","")
                setPrefData("currentTrashId","")
                setPrefData("currentLocationId","")
            }
        }

        binding.back.setOnClickListener{
            finish()
        }
        binding.ivMap.setOnClickListener{
            val moveForResultIntent = Intent(this@SellActivity, PickLocationPointActivity::class.java)
            resultLauncher.launch(moveForResultIntent)
        }

        binding.btnLanjut.setOnClickListener{
            if(getFile == null){
                binding.tvWarnTakePicture.visibility = View.VISIBLE
                binding.svSell.fullScroll(ScrollView.FOCUS_UP)
            }else if(binding.edBeratSampah.text!!.isEmpty()){
                binding.edBeratSampah.error = getString(R.string.no_empty)
            }else if(Integer.parseInt(binding.edBeratSampah.text.toString())<2 || Integer.parseInt(binding.edBeratSampah.text.toString())>10){
                binding.edBeratSampah.error = getString(R.string.weight_limit)
            }else if(namedLoc.isNullOrEmpty()){
                Toast.makeText(this@SellActivity, getString(R.string.empty_address), Toast.LENGTH_SHORT).show();
            }else{
                dashboardViewModel.getDrivers("Bearer ".plus(getPrefData("token")))
            }
            //Toast.makeText(this@SellActivity, binding.spinnerJenisSampah.selectedItem.toString(), Toast.LENGTH_SHORT).show();
        }

        dashboardViewModel.driversRes.observe(this){
            val readyDriverId: Int? = findReadyDriver(it)
            if(readyDriverId != null){
                setPrefData("currentDriverId", readyDriverId.toString())
                dashboardViewModel.postCreateTrash(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("userId").toInt(),
                    getFile!!,
                    binding.spinnerJenisSampah.selectedItem.toString(),
                    binding.edBeratSampah.text.toString(),
                    binding.edCatatan.text.toString(),
                    "$exactLocLat,$exactLocLon")
            }else{
                //there is no driver ready, make toast to tell user this then finish back to dashboard
            }
        }

        dashboardViewModel.createTrashResult.observe(this){
            if(it.message.contains("created")){
                dashboardViewModel.getTrashFromUser(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("userId").toInt()
                )
            }
        }

        dashboardViewModel.trashFromUserRes.observe(this){
            if(findBelumTrash(it)!=null){
                setPrefData("currentTrashId", findBelumTrash(it).toString())
                dashboardViewModel.putChangeTrashStatus(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("currentTrashId").toInt(),
                    "otw",
                    getPrefData("currentDriverId").toInt()
                )
            }
        }

        dashboardViewModel.changeTrashStatusRes.observe(this){
            if(it.message.contains("Status changed")){
                dashboardViewModel.getLocations("Bearer ".plus(getPrefData("token")))
            }
        }

        dashboardViewModel.locationsRes.observe(this){
            if(isLocationExist(
                    it,
                    getPrefData("currentDriverId").toInt()
                )
            ){
                dashboardViewModel.putChangeDriverStatus(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("currentDriverId"),
                    "otw"
                )
            }else{
                dashboardViewModel.postLocation(
                    "Bearer ".plus(getPrefData("token")),
                    "",
                    "",
                    getPrefData("currentDriverId").toInt()
                )
            }
        }
        dashboardViewModel.isLoading.observe(this){
            if(it){
                binding.btnLanjut.isEnabled = false
                binding.pbSell.visibility = View.VISIBLE
            }else{
                binding.btnLanjut.isEnabled = true
                binding.pbSell.visibility = View.GONE
            }
        }

        dashboardViewModel.putChangeDriverStatusRes.observe(this){
            if(it.message.contains("Success change driver status")){
                val moveWithDataIntent = Intent(this@SellActivity, ResultActivity::class.java)
                moveWithDataIntent.putExtra(ResultActivity.EXTRA_RESULT, true)
                moveWithDataIntent.putExtra("resultText",getString(R.string.success_message))
                finish()
                startActivity(moveWithDataIntent)
            }else{
                val moveWithDataIntent = Intent(this@SellActivity, ResultActivity::class.java)
                moveWithDataIntent.putExtra(ResultActivity.EXTRA_RESULT, false)
                moveWithDataIntent.putExtra("resultText",getString(R.string.failed_message))
                finish()
                startActivity(moveWithDataIntent)
            }
        }

        dashboardViewModel.postLocationRes.observe(this){
            if(it.message.contains("Location added")){
                dashboardViewModel.getLocations("Bearer ".plus(getPrefData("token")))
            }
        }

        binding.btnAmbilGambar.setOnClickListener{
            binding.tvWarnTakePicture.visibility = View.GONE
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@SellActivity,
                    "com.dicoding.pelitaapps",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val spinner: Spinner = binding.spinnerJenisSampah
        ArrayAdapter.createFromResource(
            this,
            R.array.jenis_sampah,
            android.R.layout.simple_spinner_dropdown_item
        ).also{adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Please grant camera permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun findItemIdWithStatusBelumDiambil(list: List<TrashFromUserResponseItem>): Int? {
        for (item in list) {
            if (item.status == "Belum diambil") {
                return item.id
            }
        }
        return null
    }
    fun findReadyDriver(list: List<DriversResponseItem>): Int? {
        for (item in list) {
            if (item.status == "ready") {
                setPrefData("currentDriverName", item.name)
                setPrefData("currentDriverPhone", item.phone)
                return item.id
            }
        }
        return null
    }

    fun findBelumTrash(list: List<TrashFromUserResponseItem>): Int? {
        for (item in list) {
            if (item.status == "Belum diambil") {
                return item.id
            }
        }
        return null
    }
    fun isLocationExist(list: List<LocationsResponseItem>, vDriverId: Int): Boolean {
        for (item in list) {
            if (item.driverId == vDriverId) {
                setPrefData("currentLocationId", item.id.toString())
                return true
            }
        }
        return false
    }

    fun locSplit(alamat: String): Map<String, String> {
        return mapOf(
            "lat" to alamat.split(",")[0],
            "lon" to alamat.split(",")[1]
        )
    }

    private fun getPrefData(index: String): String{
        return SettingPreference(this@SellActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@SellActivity).setPrefData(index,value).toString()
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}