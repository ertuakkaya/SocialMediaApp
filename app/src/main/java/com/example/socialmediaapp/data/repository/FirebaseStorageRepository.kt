package com.example.socialmediaapp.data.repository

import android.net.Uri
import com.example.socialmediaapp.util.ImageUploadResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(private val storage: FirebaseStorage,firestoreRepository: FirestoreRepository) {




//    private val userPofileImagesRef: StorageReference = storage.reference.child("user_profile_images")
//    private val postImagesRef: StorageReference = storage.reference.child("post_images")
//
//
//
//
//    suspend fun uploadImage(imageUri: Uri): Flow<ImageUploadResult> = flow {
//        try {
//            val filename = "${UUID.randomUUID()}" // filename should be
//            val uploadTask = userPofileImagesRef.child(filename).putFile(imageUri).await()
//            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
//            emit(ImageUploadResult(success = true, imageUrl = downloadUrl))
//        } catch (e: Exception) {
//            emit(ImageUploadResult(success = false, errorMessage = e.message))
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun getImageUrl(filename: String): Flow<String?> = flow {
//        try {
//            val url = userPofileImagesRef.child(filename).downloadUrl.await().toString()
//            emit(url)
//        } catch (e: Exception) {
//            emit(null)
//        }
//    }.flowOn(Dispatchers.IO)

    private val userProfileImagesRef: StorageReference = storage.reference.child("user_profile_images")
    private val postImagesRef: StorageReference = storage.reference.child("post_images")

    enum class ImageType {
        PROFILE,
        POST
    }

    suspend fun uploadImage(imageUri: Uri, imageType: ImageType): Flow<ImageUploadResult> = flow {
        try {
            val filename = "${UUID.randomUUID()}"
            val reference = when (imageType) {
                ImageType.PROFILE -> userProfileImagesRef
                ImageType.POST -> postImagesRef
            }
            val uploadTask = reference.child(filename).putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            emit(ImageUploadResult(success = true, imageUrl = downloadUrl))
        } catch (e: Exception) {
            emit(ImageUploadResult(success = false, errorMessage = e.message))
        }
    }.flowOn(Dispatchers.IO)

    fun getImageUrl(filename: String, imageType: ImageType): Flow<String?> = flow {
        try {
            val reference = when (imageType) {
                ImageType.PROFILE -> userProfileImagesRef
                ImageType.POST -> postImagesRef
            }
            val url = reference.child(filename).downloadUrl.await().toString()
            emit(url)
        } catch (e: Exception) {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)
}



