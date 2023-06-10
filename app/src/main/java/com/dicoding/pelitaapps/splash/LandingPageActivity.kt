package com.dicoding.pelitaapps.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.pelitaapps.MainActivity
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityLandingPageBinding
import com.dicoding.pelitaapps.login.LoginActivity

class LandingPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding

    private val splash: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splash)
    }
}