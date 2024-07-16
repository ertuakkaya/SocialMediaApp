package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.repository.FirebaseStorageRepository
import com.example.socialmediaapp.util.ImageUploadResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseStorageViewModel @Inject constructor(private val firebaseStorageRepository: FirebaseStorageRepository) : ViewModel() {

    // UI state'i tutmak için
    private val _uploadStatus = MutableStateFlow<ImageUploadResult?>(null)
    val uploadStatus: StateFlow<ImageUploadResult?> = _uploadStatus.asStateFlow()

//    suspend fun uploadImage(imageUri: Uri) = firebaseStorageRepository.uploadImage(imageUri)
//
//    suspend fun getImageUrl(filename: String) = firebaseStorageRepository.getImageUrl(filename)



//     fun uploadImageAndUpdateState(imageUri: Uri) {
//        viewModelScope.launch {
//            firebaseStorageRepository.uploadImage(imageUri).collect { result ->
//                _uploadStatus.value = result
//            }
//        }
//    }


    ////


    suspend fun uploadProfileImage(imageUri: Uri)  {
        // Profil resmi yükleme
        viewModelScope.launch {
            firebaseStorageRepository.uploadImage(imageUri, FirebaseStorageRepository.ImageType.PROFILE)
                .collect { result ->
                    _uploadStatus.value = result
                }
        }
    }

    fun uploadPostImage(imageUri: Uri)  {
        // Post resmi yükleme
        viewModelScope.launch {
            firebaseStorageRepository.uploadImage(imageUri, FirebaseStorageRepository.ImageType.POST)
                .collect { result ->
                    _uploadStatus.value = result
                }
        }
    }


    suspend fun getProfileImageUrl(filename: String) {
        // Profil resmi URL'sini alma
        viewModelScope.launch {
            firebaseStorageRepository.getImageUrl(filename, FirebaseStorageRepository.ImageType.PROFILE)
                .collect { url ->
                    // URL'yi kullan
                }
        }
    }

    suspend fun getPostImageUrl(filename: String) {
        // Post resmi URL'sini alma
        viewModelScope.launch {
            firebaseStorageRepository.getImageUrl(filename, FirebaseStorageRepository.ImageType.POST)
                .collect { url ->
                    // URL'yi kullan
                }
        }
    }


}