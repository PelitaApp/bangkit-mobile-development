package com.dicoding.pelitaapps.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.dashboard.DashboardViewModel
import com.dicoding.pelitaapps.databinding.ActivityEditProfileBinding
import com.dicoding.pelitaapps.localdata.SettingPreference

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val dashboardViewModel by viewModels<DashboardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnSimpan.setOnClickListener{
            if(binding.edName.text.toString().isEmpty()){
                binding.edName.error = getString(R.string.name_is_not_allowed_to_be_empty)
            }else if(binding.edEmail.text.toString().isEmpty()){
                binding.edEmail.error = getString(R.string.email_is_not_allowed_to_be_empty)
            }else if(binding.edPhoneNumber.text.toString().isEmpty()){
                binding.edPhoneNumber.error = getString(R.string.phone_is_not_allowed_to_be_empty)
            }else{
                dashboardViewModel.putUpdateUser(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("userId"),
                    binding.edName.text.toString(),
                    binding.edEmail.text.toString(),
                    binding.edPhoneNumber.text.toString()
                )
            }
        }
        dashboardViewModel.putUpdateUserRes.observe(this){
            if(it.message.contains("Update successful")){
                Toast.makeText(this, "Profile update is success", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "There is error while doing update", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getPrefData(index: String): String{
        return SettingPreference(this@EditProfileActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@EditProfileActivity).setPrefData(index,value).toString()
    }
}