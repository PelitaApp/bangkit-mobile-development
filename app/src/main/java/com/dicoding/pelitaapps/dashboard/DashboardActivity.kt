package com.dicoding.pelitaapps.dashboard

import android.content.Intent
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.camera.CameraActivity
import com.dicoding.pelitaapps.databinding.ActivityDashboardBinding
import com.dicoding.pelitaapps.localdata.SettingPreference
import com.dicoding.pelitaapps.payment.PaymentActivity
import com.dicoding.pelitaapps.profile.ProfileActivity
import com.dicoding.pelitaapps.sell.SellActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private val dashboardViewModel by viewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.pbGift.setProgress(83,true)
        binding.cvOtw.visibility = View.GONE
        Log.d("ea123", resources.configuration.locales[0].toString())
        binding.tvLetsCareEnvironment.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, ArticleActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnCamera.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, CameraActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnSell.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, SellActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnMenu.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(moveIntent)
        }

        binding.btnUser.setOnClickListener {
            val moveIntent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(moveIntent)
        }

        binding.cvCountPoints.setOnClickListener {
            val moveIntent = Intent(this@DashboardActivity, PaymentActivity::class.java)
            startActivity(moveIntent)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticles.layoutManager = layoutManager
        if (dashboardViewModel.listArticles.value?.size.toString() == "null"){
            dashboardViewModel.getArticles()
        }
        dashboardViewModel.listArticles.observe(this){
            setArticles(it)
        }
        SettingPreference(this@DashboardActivity).getPrefData("userId")
            ?.let { dashboardViewModel.getPoint(it.toInt(),"Bearer ".plus(SettingPreference(this@DashboardActivity).getPrefData("token"))) }
        dashboardViewModel.totalpoint.observe(this){
            binding.tvCountPoint.text = getString(R.string.count_pts,it.total.toString())
        }
    }
    private fun setArticles(itemsItem: List<ArticleResponseItem>) {
        val adapter = DashboardAdapter(itemsItem)
        binding.rvArticles.adapter = adapter
    }
}