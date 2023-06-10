package com.dicoding.pelitaapps.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.dashboard.DashboardActivity
import com.dicoding.pelitaapps.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        with(binding){
            if(intent.getBooleanExtra(EXTRA_RESULT,true)){
                ivResult.setImageResource(R.drawable.ic_check)
                tvResult.text = getString(R.string.success_message)
            }else{
                ivResult.setImageResource(R.drawable.ic_warning)
                tvResult.text = getString(R.string.failed_message)
            }
            btnBackHome.setOnClickListener{
                val moveIntent = Intent(this@ResultActivity, DashboardActivity::class.java)
                finish()
                startActivity(moveIntent)
            }
        }

    }
    companion object {
        var EXTRA_RESULT = "EXTRA_RESULT"
    }
}