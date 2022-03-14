package com.example.testassignment.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Login : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    companion object {
        private const val GOOGLE_SIGN_IN = 120
        private const val FACEBOOK_SIGN_IN = 140
    }

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
        mAuth = FirebaseAuth.getInstance()


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
            googleSignIn()
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

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                }
            }
            Log.d("Login fragment : ", exception?.message.toString())
        }
        else
            Toast.makeText(requireContext(), "Login Eror ", Toast.LENGTH_SHORT)

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("SignInActivity", "signInWithCredential:success")
                    findNavController().navigate(R.id.action_login_to_newsFeed)

                } else {
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }

}

