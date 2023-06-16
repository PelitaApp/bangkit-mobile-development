package com.dicoding.pelitaapps.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelitaapps.databinding.ActivityProfileBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var vUserName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnEdit.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnBahasa.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, LanguageActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnAbout.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, AboutActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnReview.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, ReviewActivity::class.java)
            startActivity(moveIntent)
        }
        binding.btnLogout.setOnClickListener{
            SettingPreference(this@ProfileActivity).clear()
            finishAffinity()
            val moveIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(moveIntent)
        }
        if(getPrefData("currentName") != "null"){
            binding.usernameProfile.text = getPrefData("currentName")
        }else{
            binding.usernameProfile.text = getPrefData("currentUserName")
        }

    }
    private fun getPrefData(index: String): String{
        return SettingPreference(this@ProfileActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@ProfileActivity).setPrefData(index,value).toString()
    }
}