package com.example.socialmediaapp.data.repository

import com.example.socialmediaapp.data.entitiy.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore){



    fun addUserToFirestore(user: User) = firestore.collection("users").document(user.userID ?: throw IllegalArgumentException("UserID cannot be null")).set(user)

   // fun getUserFromFirestore(userID: String) = firestore.collection("users").document(userID).get()


    fun getUserFromFirestore(userID : String) : Flow<User?> = flow {
        try {
            val snapshot = withContext(Dispatchers.IO) {
                firestore.collection("users").document(userID).get().await()
            }
            val user = snapshot.toObject(User::class.java)
            emit(user)
        } catch (e: Exception) {
            // Log error or handle it as needed
            emit(null)
        }
    }
//    fun getUserFromFirestore2(userID : String) : User? {
//
//
//        val user1 = firestore.collection("users").document(userID).get().await()
//
//        return  user1.toObject(User::class.java)
//
//    }

    fun updateUserInFirestore(userID: String, user: User) = firestore.collection("users").document(userID).update(
        mapOf(
            "userName" to user.userName,
            "email" to user.email,
            "profileImageUrl" to user.profileImageUrl,
            "name" to user.name
        )
    )

    fun deleteUserFromFirestore(userID: String) = firestore.collection("users").document(userID).delete()







}