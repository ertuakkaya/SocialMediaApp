package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userRepository = ProfileRepository()

    fun updateProfile(userName: String, imageUri: Uri?) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            var profileImageUrl = ""
            if (imageUri != null) {
                profileImageUrl = userRepository.uploadProfileImage(userId, imageUri)
            }
            userRepository.updateUserProfile(userId, userName, profileImageUrl)
        }
    }


}