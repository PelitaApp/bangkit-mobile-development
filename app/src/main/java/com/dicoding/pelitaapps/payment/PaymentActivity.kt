package com.dicoding.pelitaapps.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.dashboard.DashboardViewModel
import com.dicoding.pelitaapps.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSimpan.setOnClickListener{
            if(binding.edPoints.text.toString().isEmpty()){
                binding.edPoints.error = getString(R.string.points_is_not_allowed_to_be_empty)
            }else if(binding.edPhoneNumber.text.toString().isEmpty()){
                binding.edPhoneNumber.error = getString(R.string.phone_is_not_allowed_to_be_empty)
            }else{
                val moveWithDataIntent = Intent(this@PaymentActivity, ConfirmPaymentActivity::class.java)
                moveWithDataIntent.putExtra("points",binding.edPoints.text.toString())
                moveWithDataIntent.putExtra("phone",binding.edPhoneNumber.text.toString())
                finish()
                startActivity(moveWithDataIntent)
            }
        }
    }
}