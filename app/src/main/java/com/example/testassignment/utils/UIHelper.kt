package com.example.testassignment.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testassignment.R

@BindingAdapter("loadImage")
fun loadImageInImageView(imageView: ImageView, url : String?) {
    Glide.with(imageView).load(url)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(R.drawable.splash_logo)
        .error(R.drawable.splash_logo).into(imageView)
}

//const val URL = "https://newsapi.org/v2/everything?q=keyword"