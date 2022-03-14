package com.example.testassignment.ui.newsfeed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testassignment.databinding.NewsCardBinding
import com.example.testassignment.domain.model.NewsItem

class NewsFeedAdapter : ListAdapter<NewsItem, NewsFeedAdapter.NewsItemViewHolder>(DiffUtilCallback){

    object DiffUtilCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }

    class NewsItemViewHolder(
        private val binding : NewsCardBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(newItem: NewsItem) {
            binding.newsItem = newItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        return NewsItemViewHolder(
            binding = NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}