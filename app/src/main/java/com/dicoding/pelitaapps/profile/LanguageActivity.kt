package com.dicoding.pelitaapps.profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.window.SplashScreen
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityLanguageBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.splash.LandingPageActivity
import java.util.Locale

class LanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    fun setLanguage(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        SettingPreference(this@LanguageActivity).setPrefData("currentLanguage", language)
        val moveIntent = Intent(this@LanguageActivity, LandingPageActivity::class.java)
        finishAffinity()
        startActivity(moveIntent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentLanguage = resources.configuration.locales[0].toString()
        binding.curlang.text = currentLanguage
        if(currentLanguage=="en_us" || currentLanguage=="en_US"){
            binding.btnEn.isEnabled = false
            binding.btnIn.isEnabled = true
        }else{
            binding.btnIn.isEnabled = false
            binding.btnEn.isEnabled = true
        }
        binding.btnEn.setOnClickListener{
            setLanguage("en_US")
        }
        binding.btnIn.setOnClickListener{
            setLanguage("in")
        }
    }
}