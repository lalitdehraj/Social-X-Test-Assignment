package com.example.testassignment.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testassignment.domain.model.User
import com.example.testassignment.domain.repos.FirebaseAuthRepo

class AuthViewModel(
    private val firebaseAuthRepo: FirebaseAuthRepo
) : ViewModel(){

    fun signInUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseAuthRepo.signIn(user, onSuccess, onFailure)
    }

    fun signUpUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseAuthRepo.signUp(user, onSuccess, onFailure)
    }

    fun isUserLoggedIn() = firebaseAuthRepo.isUserLoggedIn()

    fun logout() = firebaseAuthRepo.logout()


    class Factory(
        private val firebaseAuthRepo: FirebaseAuthRepo
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel(firebaseAuthRepo) as T
        }
    }
}