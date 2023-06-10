package com.dicoding.pelitaapps.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.databinding.ActivityArticleDetailBinding

class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnBack.setOnClickListener{
            finish()
        }
        with(binding){
            tvArticleTitle.text = intent.getStringExtra(EXTRA_TITLE)
            Glide.with(ivArticle)
                .load(intent.getStringExtra(EXTRA_IMG_URL))
                .into(ivArticle)
            tvArticleDescription.text = intent.getStringExtra(EXTRA_DESCRIPTION)
            tvArticleUrlSource.text = getString(R.string.article_url_src,intent.getStringExtra(EXTRA_URL_SRC))
        }
    }

    companion object {
        val EXTRA_TITLE = "EXTRA_TITLE"
        val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        val EXTRA_IMG_URL = "EXTRA_IV_URL"
        val EXTRA_URL_SRC = "EXTRA_URL_SRC"
    }
}