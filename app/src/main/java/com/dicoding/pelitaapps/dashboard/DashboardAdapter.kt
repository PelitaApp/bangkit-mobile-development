package com.dicoding.pelitaapps.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.pelitaapps.R
import com.dicoding.pelitaapps.dashboard.ArticleDetailActivity.Companion.EXTRA_DESCRIPTION
import com.dicoding.pelitaapps.dashboard.ArticleDetailActivity.Companion.EXTRA_IMG_URL
import com.dicoding.pelitaapps.dashboard.ArticleDetailActivity.Companion.EXTRA_TITLE
import com.dicoding.pelitaapps.dashboard.ArticleDetailActivity.Companion.EXTRA_URL_SRC

class DashboardAdapter(private val listReview: List<ArticleResponseItem>, private val isVertical: Int = 0) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return if(isVertical==0){
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.article_item, viewGroup, false))
        }else{
            ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.article_item_vertical, viewGroup, false))
        }
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvTitle.text = listReview[position].title
        Glide.with(viewHolder.imageView)
            .load(listReview[position].thumbnail)
            .into(viewHolder.imageView)
        if(isVertical == 1){
            viewHolder.tvDescription?.text = listReview[position].text
        }

        viewHolder.itemView.setOnClickListener{
            val moveIntent = Intent(viewHolder.itemView.context, ArticleDetailActivity::class.java)
            moveIntent.putExtra(EXTRA_TITLE,listReview[position].title)
            moveIntent.putExtra(EXTRA_IMG_URL,listReview[position].thumbnail)
            moveIntent.putExtra(EXTRA_DESCRIPTION,listReview[position].text)
            moveIntent.putExtra(EXTRA_URL_SRC,listReview[position].link)
            viewHolder.itemView.context.startActivity(moveIntent)
        }

    }
    override fun getItemCount() = listReview.size
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_article_title)
        val imageView: ImageView = view.findViewById(R.id.iv_item)
        val tvDescription: TextView? = view.findViewById(R.id.tv_article_description)
    }


}