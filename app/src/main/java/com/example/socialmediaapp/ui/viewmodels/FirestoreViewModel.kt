package com.example.socialmediaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirestoreViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {

    fun addUserToFirestore(user : User) = firestoreRepository.addUserToFirestore(user)

    //fun getUserFromFirestore(userID: String) = firestoreRepository.getUserFromFirestore(userID)

    ///
    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun getUserFromFirestore(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                firestoreRepository.getUserFromFirestore(userId).collect { user ->
                    _userData.value = user
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error fetching user data: ${e.message}"
                _isLoading.value = false
            }
        }
    }

//    fun getUserFromFirestore2(userId: String) : User {
//        return firestoreRepository.getUserFromFirestore(userId)
//    }
    ///


    fun updateUserInFirestore(userID: String, user: User) {
        firestoreRepository.updateUserInFirestore(userID, user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle success

            } else {
                // Handle failure
            }
        }
    }

    fun deleteUserFromFirestore(userID: String) {
        firestoreRepository.deleteUserFromFirestore(userID).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle success

            } else {
                // Handle failure
            }
        }
    }





}