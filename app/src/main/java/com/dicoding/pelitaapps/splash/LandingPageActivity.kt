package com.dicoding.pelitaapps.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.pelitaapps.MainActivity
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityLandingPageBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.login.LoginActivity
import java.util.Locale

class LandingPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding

    private val splash: Long = 1000
    fun setLanguage(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentLanguage: String = resources.configuration.locales[0].toString()
        val currentPrefLanguage: String? = SettingPreference(this@LandingPageActivity).getPrefData("currentLanguage")
        if (currentPrefLanguage != null) {
            if (currentLanguage.lowercase() != currentPrefLanguage.lowercase()){
                setLanguage(currentPrefLanguage)
                recreate()
            }
        }
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splash)
    }
}