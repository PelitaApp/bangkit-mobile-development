package com.dicoding.pelitaapps.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelitaapps.databinding.ActivityReviewBinding

class ReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener{
            finish()
        }
        binding.btnBack.setOnClickListener{
            finish()
        }
    }
}