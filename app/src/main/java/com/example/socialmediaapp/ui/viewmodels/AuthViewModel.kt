package com.example.socialmediaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    fun getCurrentUser() = authRepository.getCurrentUser()

    fun login(email: String, password: String) = authRepository.login(email, password)

    fun signUp(email: String, password: String) = authRepository.signUp(email, password)

    fun sendPasswordResetEmail(email: String) = authRepository.sendPasswordResetEmail(email)

    fun sendEmailVerification() = authRepository.sendEmailVerification()

    fun getUserEmail() = authRepository.getUserEmail()

    fun deleteUser() = authRepository.deleteUser()

    fun signOut() = authRepository.signOut()


}