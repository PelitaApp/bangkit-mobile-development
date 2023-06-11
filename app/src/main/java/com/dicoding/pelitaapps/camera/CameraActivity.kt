package com.dicoding.pelitaapps.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.pelitaapps.createCustomTempFile
import com.dicoding.pelitaapps.databinding.ActivityCameraBinding
import java.io.File

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private var namedLoc: String? = ""
    private var exactLocLat: String? = ""
    private var exactLocLon: String? = ""
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.ivCamera.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }else{
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if (!allPermissionsGranted()) {
            Log.d("perm","not get any")
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        if(allPermissionsGranted()){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createCustomTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@CameraActivity,
                    "com.dicoding.pelitaapps",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
        binding.back.setOnClickListener{
            finish()
        }
        binding.btnRetake.setOnClickListener{
            val moveIntent = Intent(this@CameraActivity, CameraActivity::class.java)
            finish()
            startActivity(moveIntent)
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
            }else{
                val moveIntent = Intent(this@CameraActivity, CameraActivity::class.java)
                finish()
                startActivity(moveIntent)
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