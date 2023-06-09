package com.dicoding.pelitaapps.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}