package com.example.socialmediaapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth : FirebaseAuth){

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun login(email: String, password: String) = auth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)

    fun sendPasswordResetEmail(email: String) = auth.sendPasswordResetEmail(email)

    fun sendEmailVerification() = auth.currentUser?.sendEmailVerification()

    fun getUserEmail(): String = auth.currentUser?.email ?: ""

    fun deleteUser() = auth.currentUser?.delete()

    fun signOut() = auth.signOut()
}