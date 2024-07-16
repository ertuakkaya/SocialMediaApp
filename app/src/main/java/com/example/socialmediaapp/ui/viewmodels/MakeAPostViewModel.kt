package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.repository.FirebaseStorageRepository
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
import javax.inject.Inject

@HiltViewModel
class MakeAPostViewModel @Inject constructor(private val firebaseStorageRepository: FirebaseStorageRepository, private val postRepository: PostRepository) : ViewModel() {


    // UI state'i tutmak için
    private val _uploadStatus = MutableStateFlow<ImageUploadResult?>(null)
    val uploadStatus: StateFlow<ImageUploadResult?> = _uploadStatus.asStateFlow()


    fun uploadImageAndPost(imageUri: Uri, post: Post) : Flow<PostResult> = flow{
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