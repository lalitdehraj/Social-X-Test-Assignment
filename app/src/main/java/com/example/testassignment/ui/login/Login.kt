package com.example.testassignment.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testassignment.R
import com.example.testassignment.databinding.FragmentLoginBinding
import com.example.testassignment.domain.model.User
import com.example.testassignment.domain.repos.FirebaseAuthRepo
import com.example.testassignment.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Login : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    private val authViewModel by lazy {
        ViewModelProvider(
            this, AuthViewModel.Factory(
                firebaseAuthRepo = FirebaseAuthRepo.get()
            )
        )[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.lifecycleOwner = this

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_newsFeed)
        }

        binding.signupTab.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signUp)
        }

        binding.loginButton.setOnClickListener {
            proceedSignIn()
            binding.loginButton.isClickable = false
            lifecycleScope.launch {
                delay(1500)
                binding.loginButton.isClickable = true
            }
        }

        binding.googleSigninImage.setOnClickListener({

        })
    }

    private fun proceedSignIn() {
        val email = binding.emailIdEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()

        if (!(email != "" && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(requireContext(), "Please input a Valid Email", Toast.LENGTH_SHORT)
                .show()
            return
        } else if (password == "") {
            Toast.makeText(requireContext(), "Please input a Valid Password", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val user = User("", email, password)

        authViewModel.signInUser(
            user = user,
            onSuccess = {
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_login_to_newsFeed)
            },
            onFailure = {
                Toast.makeText(requireContext(), "User authentication failed", Toast.LENGTH_SHORT)
                    .show()
            }
        )

    }



}

