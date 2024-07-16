package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.repository.FirebaseStorageRepository
import com.example.socialmediaapp.data.repository.FirebaseStorageRepository.ImageType
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.example.socialmediaapp.data.repository.PostRepository
import com.example.socialmediaapp.util.ImageUploadResult
import com.example.socialmediaapp.util.PostResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FirebaseStorageViewModel @Inject constructor(
    private val firebaseStorageRepository: FirebaseStorageRepository,
    private val postRepository: PostRepository
) : ViewModel() {

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

    fun uploadImageAndPost(imageUri: Uri, post: Post) : Flow<PostResult>  = flow{
        try {
            firebaseStorageRepository.uploadImage(imageUri, FirebaseStorageRepository.ImageType.POST)
                .collect { result ->
                    _uploadStatus.value = result
                }
            postRepository.createPost(post)

            emit(PostResult(success = true))

        }catch (e: Exception){
            emit(PostResult(success = false, errorMessage = e.message))
        }

//        viewModelScope.launch {
//            firebaseStorageRepository.uploadImage(imageUri, FirebaseStorageRepository.ImageType.POST)
//                .collect { result ->
//                    _uploadStatus.value = result
//                }
//            postRepository.createPost(post)

        //}
    }.flowOn(Dispatchers.IO)




}