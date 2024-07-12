package com.example.socialmediaapp.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class FirebaseViewModel @Inject constructor( private val auth : FirebaseAuth): ViewModel() {

    //private val auth : FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState



    // when app launches, check if user is authenticated
    init {
        checkAuthStatus()
    }

    // Kullanıcının oturum durumunu kontrol eden fonksiyon
    fun checkAuthStatus(){

        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }


    }

    // Kullanıcının giriş yapmasını sağlayan fonksiyon
    // .signInWithEmailAndPassword(email,password) -> Bu fonksiyon firebase üzerinde kullanıcı girişi yapar
    fun login(email : String,password : String){

        // Eğer email veya password boş ise hata mesajı döndür ve fonksiyonu sonlandır
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password must not be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }

            }



    }




    // Kullanıcının kayıt olmasını sağlayan fonksiyon
    // .createUserWithEmailAndPassword(email,password) -> Bu fonksiyon firebase üzerinde kullanıcı oluşturur
    fun signUp(email : String,password : String){

        // Eğer email veya password boş ise hata mesajı döndür ve fonksiyonu sonlandır
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password must not be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }

            }





    }

    // send a password reset email
    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    // email verification
    fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    // get user's email
    fun getUserEmail() : String {
        return auth.currentUser?.email ?: ""
    }




    // delete user
    fun deleteUser() {
        auth.currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }


    // Kullanıcının çıkış yapmasını sağlayan fonksiyon
    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    val user = auth.currentUser
    // update user profile
    fun updateUserProfile( displayName : String, photoUri : Uri){
    // [START update_profile]

        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
            //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            this.photoUri = photoUri
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
                }
    // [END update_profile]
    }

    fun getUserProfile(){
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl // Uri
            }
        }
    }






}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}