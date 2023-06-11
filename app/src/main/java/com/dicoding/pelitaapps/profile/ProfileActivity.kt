package com.dicoding.pelitaapps.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBahasa.setOnClickListener{
            val moveIntent = Intent(this@ProfileActivity, LanguageActivity::class.java)
            startActivity(moveIntent)
        }
    }
}