package com.dicoding.pelitaapps.sell

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.createCustomTempFile
import com.dicoding.pelitaapps.databinding.ActivitySellBinding
import com.dicoding.pelitaapps.result.ResultActivity
import java.io.File

class SellActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellBinding
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
            Log.d("ea2","asd")
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
        binding.back.setOnClickListener{
            finish()
        }
        binding.ivMap.setOnClickListener{
            val moveForResultIntent = Intent(this@SellActivity, PickLocationPointActivity::class.java)
            resultLauncher.launch(moveForResultIntent)
        }

        binding.btnLanjut.setOnClickListener{
            if(binding.edBeratSampah.text!!.isEmpty()){
                binding.edBeratSampah.error = getString(R.string.no_empty)
            }else if(Integer.parseInt(binding.edBeratSampah.text.toString())<2 || Integer.parseInt(binding.edBeratSampah.text.toString())>10){
                binding.edBeratSampah.error = getString(R.string.weight_limit)
            }else if(namedLoc == ""){
                Toast.makeText(this@SellActivity, getString(R.string.empty_address), Toast.LENGTH_SHORT).show();
            }else{
                val moveWithDataIntent = Intent(this@SellActivity, ResultActivity::class.java)
                moveWithDataIntent.putExtra(ResultActivity.EXTRA_RESULT, true)
                finish()
                startActivity(moveWithDataIntent)
            }
            //Toast.makeText(this@SellActivity, binding.spinnerJenisSampah.selectedItem.toString(), Toast.LENGTH_SHORT).show();
        }

        binding.btnAmbilGambar.setOnClickListener{
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
            Log.d("perm","not get any")
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

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}