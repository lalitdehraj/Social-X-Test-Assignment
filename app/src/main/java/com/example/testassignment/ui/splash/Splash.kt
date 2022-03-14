package com.example.testassignment.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testassignment.R
import com.example.testassignment.domain.repos.FirebaseAuthRepo
import com.example.testassignment.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

class Splash : Fragment(R.layout.fragment_splash) {
    private val authViewModel by lazy {
        ViewModelProvider(
            this, AuthViewModel.Factory(
                firebaseAuthRepo = FirebaseAuthRepo.get()
            )
        )[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isUserLoggedIn = authViewModel.isUserLoggedIn()

        lifecycleScope.launchWhenStarted {
            delay(2000)
            if (isUserLoggedIn) {
                findNavController().navigate(R.id.action_splash_to_newsFeed)
            } else {
                findNavController().navigate(R.id.action_splash_to_login)
            }
        }

    }
}