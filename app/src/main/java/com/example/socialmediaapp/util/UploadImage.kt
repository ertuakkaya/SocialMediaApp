package com.example.socialmediaapp.util

import android.net.Uri
import android.util.Log
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

suspend fun uploadFile(uri : Uri, fileName : String, imagePath : String) : Uri {
    val firebaseStorage = FirebaseStorage.getInstance()

    var file = uri
    val uploadTask = firebaseStorage.reference.child("$imagePath/$fileName").putFile(file)

    file = uploadTask.addOnFailureListener { e ->
        Log.w("uploadFile", "uploadFile: ", e)
    }.addOnSuccessListener { taskSnapshot ->
        taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
            Log.d("uploadFile", "uploadFile: ${it}")
        }
    }.await().metadata?.reference?.downloadUrl?.await()!!

    return file

}


suspend fun uploadProfileImage(
    uri: Uri,
    user: User?,
    userViewModel: UserViewModel,
    firestoreViewModel: FirestoreViewModel
): User? {
    val filename = UUID.randomUUID().toString()
    Log.d("AccountScreenBodyContent", "Selected Image URI: $uri")

    val imageUrl = userViewModel.uploadProfilePicture(
        uri,
        filename,
        "user_profile_images"
    )
    Log.d("AccountScreenBodyContent imageUrl", ": $imageUrl")

    return user?.let { nonNullUser ->
        val updatedUser = nonNullUser.copy(profileImageUrl = imageUrl.toString())
        firestoreViewModel.updateUserInFirestore(updatedUser.userID!!, updatedUser)
        Log.d("AccountScreen Profile Updated", "AccountScreenBodyContent: $updatedUser")
        updatedUser
    }
}