package com.example.testassignment.ui.newsfeed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testassignment.R
import com.example.testassignment.databinding.FragmentNewsFeedBinding
import com.example.testassignment.domain.repos.FirebaseAuthRepo
import com.example.testassignment.domain.repos.NewsFeedRepo
import com.example.testassignment.ui.viewmodels.AuthViewModel
import com.example.testassignment.ui.viewmodels.NewsFeedViewModel


class NewsFeed : Fragment(R.layout.fragment_news_feed) {
    private lateinit var binding : FragmentNewsFeedBinding
    private val newsAdapter by lazy { NewsFeedAdapter() }

    private val viewModel by lazy {
        ViewModelProvider(this,NewsFeedViewModel.Factory(NewsFeedRepo.get(requireContext())))[NewsFeedViewModel::class.java]
    }
    private val authViewModel by lazy {
        ViewModelProvider(this, AuthViewModel.Factory(
            firebaseAuthRepo = FirebaseAuthRepo.get()
        ))[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsFeedBinding.bind(view)
        binding.lifecycleOwner = this


        binding.newsList.adapter = newsAdapter

        viewModel.fetchNewsArticles(
            onSuccess = {
                binding.emptyListMsg.visibility = View.GONE
                newsAdapter.submitList(it) },

            onFailure = {
                binding.emptyListMsg.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "${it.statusCode} : ${it.data}", Toast.LENGTH_LONG).show()
            }
        )

        binding.logout.setOnClickListener {
            authViewModel.logout()
            findNavController().navigate(R.id.action_newsFeed_to_splash)
        }
    }
}