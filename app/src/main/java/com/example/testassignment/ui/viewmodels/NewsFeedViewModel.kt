package com.example.testassignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.NetworkResponse
import com.example.testassignment.domain.model.NewsItem
import com.example.testassignment.domain.repos.NewsFeedRepo

class NewsFeedViewModel(
    val newsFeedRepo: NewsFeedRepo
) : ViewModel() {

    fun fetchNewsArticles(onSuccess: ( List<NewsItem>) -> Unit,
    onFailure: (NetworkResponse) -> Unit){
        newsFeedRepo.fetchNewsArticles(onSuccess,onFailure)
    }



    class Factory(
        private val newsFeedRepo: NewsFeedRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NewsFeedViewModel(newsFeedRepo) as T
        }
    }
}