package com.dicoding.pelitaapps.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.pelitaapps.MainActivity
import com.dicoding.pelitaapps.R

class LandingPageActivity : AppCompatActivity() {

    private val splash: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splash)
    }
}