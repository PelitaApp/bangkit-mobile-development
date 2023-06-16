package com.dicoding.pelitaapps.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelitaapps.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}