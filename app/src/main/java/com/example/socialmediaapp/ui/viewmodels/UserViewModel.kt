package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.AuthRepository
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.example.socialmediaapp.util.uploadFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    suspend fun uploadProfilePicture(imageUri: Uri, fileName : String , imagePath : String) : Uri? {
        return uploadFile(imageUri, fileName, imagePath)
    }




}