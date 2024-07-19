package com.example.socialmediaapp.util

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

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