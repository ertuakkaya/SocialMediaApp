package com.example.socialmediaapp.ui.viewmodels

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.AuthRepository
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    //private val auth : FirebaseAuth = FirebaseAuth.getInstance()


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    // when app launches, check if user is authenticated
    init {
        checkAuthStatus()
    }

    // Kullanıcının oturum durumunu kontrol eden fonksiyon
    fun checkAuthStatus() {

        if (authRepository.getCurrentUser() == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }


    }


    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password must not be empty")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                authRepository.login(email, password).await()
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
        }
    }


    fun signUp(
        email: String,
        password: String,
        userName: String,
        name: String? = null,
        profileImageUrl: String? = null
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password must not be empty")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = authRepository.signUp(email, password).await()
                val user = authRepository.getCurrentUser()
                if (user != null) {
                    firestoreRepository.addUserToFirestore(
                        User(
                            userID = user.uid,
                            userName = userName,
                            email = user.email.toString(),
                            profileImageUrl = profileImageUrl,
                            name = name
                        )
                    )
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("User creation successful but user is null")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
        }
    }


    // Kullanıcının çıkış yapmasını sağlayan fonksiyon
    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState.Unauthenticated
    }


    /*
       fun addUserToFirestore(
           userName: String,
           profileImageUrl: String?,
           userID: String,
           email: String,
           name: String?
       ) {
           val user = User(
               userID = userID,
               userName = userName,
               email = email,
               profileImageUrl = profileImageUrl,
               name = name
           )
           firestore.collection("users").document(userID).set(user)
               .addOnSuccessListener {
                   Log.d("Firestore", "User added with ID: $userID")
               }
               .addOnFailureListener {
                   e -> Log.w("Firestore", "Error adding user", e)
               }
       }
       */


    /*
      // send a password reset email
      fun sendPasswordResetEmail(email: String) {
          auth.sendPasswordResetEmail(email)
              .addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                      _authState.value = AuthState.Authenticated
                  } else {
                      _authState.value =
                          AuthState.Error(task.exception?.message ?: "Something went wrong")
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
                      _authState.value =
                          AuthState.Error(task.exception?.message ?: "Something went wrong")
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
                      _authState.value =
                          AuthState.Error(task.exception?.message ?: "Something went wrong")
                  }
              }
      }

      */


}

