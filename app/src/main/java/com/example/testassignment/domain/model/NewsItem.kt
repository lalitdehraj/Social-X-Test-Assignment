package com.example.testassignment.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class NewsItem (
    var author: String? ,
    var title: String ,
    var description: String,
    var urlToImage: String? ,
    var publishedAt: Date
){
    companion object{
        fun sample() = NewsItem(
            author = "Ankit",
            title = "${Random().nextInt()}",
            description = "Description : ${Random().nextInt()}",
            urlToImage = null,
            publishedAt = Date()
        )
    }

    fun getDateAsString() : String {

        val format = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return format.format(publishedAt)
    }
}

data class News(
    var articles: List<NewsItem>
)