package com.example.socialmediaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.AuthRepository
import com.example.socialmediaapp.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

//    fun getUserInfoByID(userID: String) {
//        firestoreRepository.getUserFromFirestore(userID).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                _userState.value = task.result.toObject(User::class.java)
//            } else {
//                _userState.value = null
//            }
//        }
//    }
}