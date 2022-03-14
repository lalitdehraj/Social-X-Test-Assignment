package com.example.testassignment.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testassignment.R
import com.example.testassignment.databinding.FragmentSignUpBinding
import com.example.testassignment.domain.model.User
import com.example.testassignment.domain.repos.FirebaseAuthRepo
import com.example.testassignment.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUp : Fragment(R.layout.fragment_sign_up){
    private lateinit var binding: FragmentSignUpBinding

    private val authViewModel by lazy {
        ViewModelProvider(
            this, AuthViewModel.Factory(
                firebaseAuthRepo = FirebaseAuthRepo.get()
            )
        )[AuthViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        binding.lifecycleOwner = this

        binding.registerButton.setOnClickListener { proceedSignUp()
        binding.registerButton.isClickable= false
        lifecycleScope.launch {
            delay(1500)
            binding.registerButton.isClickable= true

        }}
        binding.loginTab.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_login)
        }
    }

    private fun proceedSignUp() {
        val name = binding.nameEdittext.text.toString()
        val email = binding.emailEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()

        val user = User(name, email, password)
        authViewModel.signUpUser(
            user = user,
            onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Account created successfully, please login to continue",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_signUp_to_login)
            },
            onFailure = {
                Toast.makeText(requireContext(), "User authentication failed", Toast.LENGTH_SHORT)
                    .show()
            }
        )

    }
}