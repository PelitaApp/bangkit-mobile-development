package com.dicoding.pelitaapps.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.pelitaapps.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {
    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private lateinit var binding: ActivityArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnBack.setOnClickListener{
            finish()
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvArticles.layoutManager = layoutManager
        if (dashboardViewModel.listArticles.value?.size.toString() == "null"){
            dashboardViewModel.getArticles()
        }
        dashboardViewModel.listArticles.observe(this){
            setArticles(it)
        }
    }

    private fun setArticles(itemsItem: List<ArticleResponseItem>) {
        val adapter = DashboardAdapter(itemsItem,1)
        binding.rvArticles.adapter = adapter
    }
    companion object {
        val EXTRA_ARTICLES = "EXTRA_ARTICLES"
    }
}