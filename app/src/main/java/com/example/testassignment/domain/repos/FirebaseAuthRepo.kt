package com.example.testassignment.domain.repos

import android.provider.Settings.Global.getString
import com.example.testassignment.R
import com.example.testassignment.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseAuthRepo private constructor() {
    companion object {
        @Volatile
        var INSTANCE: FirebaseAuthRepo? = null

        fun get(): FirebaseAuthRepo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = FirebaseAuthRepo()
                INSTANCE!!
            }
        }
    }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var googleSignInClient: GoogleSignInClient


    fun signIn(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->

                // Sign up successful
                if (task.isSuccessful) onSuccess()
                else onFailure()
            }
    }

    fun signUp(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->

                // Sign up successful
                if (task.isSuccessful) updateProfileDetails(user.name, onSuccess, onFailure)
                else onFailure()
            }

    }

    private fun updateProfileDetails(name: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val user = firebaseAuth.currentUser

        val profileUpdates: UserProfileChangeRequest =
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    logout()
                } else onFailure()
            }
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout() = firebaseAuth.signOut()



}