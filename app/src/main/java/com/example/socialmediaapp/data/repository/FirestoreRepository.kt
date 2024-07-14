package com.example.socialmediaapp.data.repository

import com.example.socialmediaapp.data.entitiy.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore){



    fun addUserToFirestore(user: User) = firestore.collection("users").document(user.userID ?: throw IllegalArgumentException("UserID cannot be null")).set(user)

    fun getUserFromFirestore(userID: String) = firestore.collection("users").document(userID).get()

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