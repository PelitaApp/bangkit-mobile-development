package com.dicoding.pelitaapps.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.dashboard.DashboardActivity
import com.dicoding.pelitaapps.databinding.ActivityProfileBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.login.LoginActivity
import com.dicoding.pelitaapps.splash.LandingPageActivity
import com.dicoding.pelitaapps.viewmodel.MainViewModel
import com.dicoding.pelitaapps.viewmodel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingPreference: SettingPreference
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreference = SettingPreference(this)

        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = binding.usernameProfile.text.toString()
        binding.usernameProfile.setText(username)


        binding.btnBahasa.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, LanguageActivity::class.java)
            startActivity(moveIntent)
        }


        binding.btnEdit.setOnClickListener {
            val moveIntent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnBack.setOnClickListener {
            val moveIntent = Intent(this@ProfileActivity, DashboardActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        // Menghapus data sesi menggunakan SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Arahkan pengguna ke layar login atau halaman lain yang sesuai
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        
        binding.btnLogout.setOnClickListener{
            SettingPreference(this@ProfileActivity).setPrefData("token","")
            finishAffinity()
            val moveIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(moveIntent)
        }
    }
}