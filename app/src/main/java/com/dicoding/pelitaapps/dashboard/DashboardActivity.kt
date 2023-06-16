package com.dicoding.pelitaapps.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
    private var totalTrashKg: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.pbGift.setProgress(83,true)
        binding.tvXxPercentAgain.text = getString(R.string.count_gift_percent,83.toString()).plus("%")
        binding.cvOtw.visibility = View.GONE
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
        if(getPrefData("currentUserName").isEmpty()){
            dashboardViewModel.getUser(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("userId")
            )
        }
        dashboardViewModel.getUserRes.observe(this){
            setPrefData("currentUserName",it.username.toString())
            setPrefData("currentName",it.name.toString())
            setPrefData("currentEmail",it.email.toString())
            setPrefData("currentPhone",it.phone.toString())
        }
        if(getPrefData("currentTrashId").isNotEmpty()){
            dashboardViewModel.getTrashFromTrashId(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("currentTrashId")
            )
            dashboardViewModel.getTrashFromUser(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("userId").toInt()
            )
        }else if(getPrefData("currentTrashId").isEmpty()){
            dashboardViewModel.getTrashFromUser(
                "Bearer ".plus(getPrefData("token")),
                getPrefData("userId").toInt()
            )
        }

        dashboardViewModel.trashFromUserRes.observe(this){
            for(item in it){
            }
            if(findOtwTrash(it)){
                dashboardViewModel.getLocations("Bearer ".plus(getPrefData("token")))
            }
            for(item in it){
                totalTrashKg += item.weight
            }
            binding.tvCountTrashKg.text = getString(R.string.count_kg,totalTrashKg.toString())
        }

        dashboardViewModel.locationsRes.observe(this){
            if(isLocationExist(
                    it,
                    getPrefData("currentDriverId").toInt()
                )){
                if(getPrefData("currentTrashId").isNotEmpty()){
                    dashboardViewModel.getTrashFromTrashId(
                        "Bearer ".plus(getPrefData("token")),
                        getPrefData("currentTrashId")
                    )
                }
            }
        }

        dashboardViewModel.getTrashFromTrashIdRes.observe(this){
            if(it.status.contains("otw")){
                setPrefData("trashLocationLatLon", it.address)
                binding.cvOtw.visibility = View.VISIBLE
                dashboardViewModel.getDriverFromId(
                    "Bearer ".plus(getPrefData("token")),
                        getPrefData("currentDriverId")
                )
                dashboardViewModel.getDriverLocation(
                    "Bearer ".plus(getPrefData("token")),
                    getPrefData("currentLocationId")
                )
            }else{
                setPrefData("currentDriverId","")
                setPrefData("currentTrashId","")
                setPrefData("currentLocationId","")
            }
        }
        dashboardViewModel.getDriverFromIdRes.observe(this){
            setPrefData("currentDriverName", it.name)
            setPrefData("currentDriverPhone", it.phone)
        }
        dashboardViewModel.getDriverLocationRes.observe(this){
            setPrefData("currentDriverLocationLatLon", "${it.lat},${it.lon}")
            dashboardViewModel.getDirection("${it.lat},${it.lon}", getPrefData("trashLocationLatLon"), getString(R.string.google_maps_api_key))
        }
        dashboardViewModel.getDirectionRes.observe(this){
            binding.tvOtwEta.text = getString(R.string.otw_eta,toTime((it.routes?.get(0)?.legs?.get(0)?.duration?.value!!)))
        }
        binding.cvOtw.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, TrackDriverActivity::class.java)
            startActivity(moveIntent)
        }
        binding.cvCountPoints.setOnClickListener{
            val moveIntent = Intent(this@DashboardActivity, PaymentActivity::class.java)
            startActivity(moveIntent)
        }
    }

    fun findOtwTrash(list: List<TrashFromUserResponseItem>): Boolean {
        for (item in list) {
            if (item.status == "otw") {
                setPrefData("currentTrashId", item.id.toString())
                setPrefData("currentDriverId", item.driverId.toString())
                return true
            }
        }
        return false
    }

    fun isLocationExist(list: List<LocationsResponseItem>, vDriverId: Int): Boolean {
        for (item in list) {
            if (item.driverId == vDriverId) {
                setPrefData("currentLocationId", item.id.toString())
                return true
            }
        }
        return false
    }
    fun findReadyDriver(list: List<DriversResponseItem>): Int? {
        for (item in list) {
            if (item.status == "ready") {
                setPrefData("currentDriverName", item.name)
                setPrefData("currentDriverPhone", item.phone)
                return item.id
            }
        }
        return null
    }

    fun toTime(seconds: Int): String{
        return "${seconds/3600}h ${(seconds-(3600*(seconds/3600)))/60}m ${seconds - (((seconds/3600)*3600)+(((seconds-(3600*(seconds/3600)))/60)*60))}s"
    }
    private fun getPrefData(index: String): String{
        return SettingPreference(this@DashboardActivity).getPrefData(index).toString()
    }
    private fun setPrefData(index: String, value: String): String{
        return SettingPreference(this@DashboardActivity).setPrefData(index,value).toString()
    }
    private fun setArticles(itemsItem: List<ArticleResponseItem>) {
        val adapter = DashboardAdapter(itemsItem)
        binding.rvArticles.adapter = adapter
    }
}