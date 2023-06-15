package com.dicoding.pelitaapps.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityPaymentBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.profile.ProfileActivity

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var SettingPreference: SettingPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaymentBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)





        binding.btnPencairan.setOnClickListener {
            val moveIntent = Intent(this@PaymentActivity, PaymentSuccesActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnMenu.setOnClickListener{
            val moveIntent = Intent(this, ProfileActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnUser.setOnClickListener {
            val moveIntent = Intent(this, ProfileActivity::class.java)
            startActivity(moveIntent)
        }
    }
}