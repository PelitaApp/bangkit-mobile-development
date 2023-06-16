package com.dicoding.pelitaapps.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelitaapps.dashboard.DashboardViewModel
import com.dicoding.pelitaapps.databinding.ActivityConfirmPaymentBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.result.ResultActivity

class ConfirmPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmPaymentBinding
    private val dashboardViewModel by viewModels<DashboardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.pbCp.visibility = View.GONE

        val points: String = intent.getStringExtra("points").toString()
        val phone: String = intent.getStringExtra("phone").toString()

        binding.btnSimpan.setOnClickListener{
            dashboardViewModel.postLoginUser(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("currentEmail"),
                binding.etEditTextPassword.text.toString()
            )
        }
        dashboardViewModel.postLoginUserRes.observe(this){
            if(it.message.contains("Login successful",true)){
                dashboardViewModel.postTakenPoint(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("userId"),
                    points!!,
                    phone!!
                )
            }else{
                val moveWithDataIntent = Intent(this@ConfirmPaymentActivity, ResultActivity::class.java)
                moveWithDataIntent.putExtra(ResultActivity.EXTRA_RESULT, false)
                moveWithDataIntent.putExtra("resultText","Failed! The password you input is not correct")
                finish()
                startActivity(moveWithDataIntent)
            }
        }
        dashboardViewModel.postTakenPointRes.observe(this){
            val moveWithDataIntent = Intent(this@ConfirmPaymentActivity, ResultActivity::class.java)
            moveWithDataIntent.putExtra(ResultActivity.EXTRA_RESULT, true)
            moveWithDataIntent.putExtra("resultText","Success! The payment will come in 1x24")
            finish()
            startActivity(moveWithDataIntent)
        }
        dashboardViewModel.isLoading.observe(this){
            if(it){
                binding.btnSimpan.isEnabled = false
                binding.pbCp.visibility = View.VISIBLE
            }else{
                binding.btnSimpan.isEnabled = true
                binding.pbCp.visibility = View.GONE
            }
        }
    }
    private fun getPrefData(index: String): String{
        return SettingPreference(this@ConfirmPaymentActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@ConfirmPaymentActivity).setPrefData(index,value).toString()
    }
}