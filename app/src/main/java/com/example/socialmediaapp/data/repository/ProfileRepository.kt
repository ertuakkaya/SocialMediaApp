package com.example.socialmediaapp.data.repository
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
class ProfileRepository {



    private val storage = Firebase.storage
    private val storageRef = storage.reference

    suspend fun uploadProfileImage(userId: String, imageUri: Uri): String {
        // Varsayılan bucket'ta kullanıcı ID'sine göre dosya yolu oluşturuyoruz
        val imageRef = storageRef.child("user-profile-images/$userId/profile-image.jpg")
        val uploadTask = imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }

    suspend fun updateUserProfile(userId: String, userName: String, profileImageUrl: String) {
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(userId)
        userDoc.update(mapOf(
            "userName" to userName,
            "profileImageUrl" to profileImageUrl
        )).await()
    }

}