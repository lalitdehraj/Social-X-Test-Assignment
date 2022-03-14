package com.example.testassignment.domain.repos

import android.content.Context
import android.net.Uri
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.testassignment.domain.model.News
import com.example.testassignment.domain.model.NewsItem
import com.example.testassignment.utils.URL
import com.google.gson.Gson

class NewsFeedRepo private constructor(private val requestQueue: RequestQueue) {
    companion object {
        @Volatile
        private var INSTANCE: NewsFeedRepo? = null

        fun get(context: Context): NewsFeedRepo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = NewsFeedRepo(Volley.newRequestQueue(context))
                INSTANCE!!
            }
        }
    }

    fun fetchNewsArticles(
        onSuccess: (List<NewsItem>) -> Unit,
        onFailure: (NetworkResponse) -> Unit
    ) {
        val newsArticlesRequest = object : StringRequest(
            Request.Method.GET,
            Uri.parse(URL).toString(),
            {
                val news: News = Gson().fromJson(it, News::class.java)
                onSuccess(news.articles)
            },
            {
                onFailure(it.networkResponse)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Api-Key"] = "5df7ffce89c64305b7843aab14523344"
                headers["Content-Type"] = "application/form-data"
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        requestQueue.add(newsArticlesRequest)
    }
}