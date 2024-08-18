package com.example.socialmediaapp.data.repository

import android.util.Log
import com.example.socialmediaapp.data.entitiy.ChatMessage
import com.example.socialmediaapp.data.entitiy.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val firestore: FirebaseFirestore){


    /**
     * Fetch all users from Firestore
     */
    suspend fun fetchUsers() : Flow<List<User>> = callbackFlow{
        val receivedMessageListener = userCollection.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val users = snapshot?.toObjects(User::class.java) ?: emptyList()
            trySend(users)

        }
        awaitClose {
            receivedMessageListener.remove()
        }


    }


    fun addUserToFirestore(user: User) = firestore.collection("users").document(user.userID ?: throw IllegalArgumentException("UserID cannot be null")).set(user)

   // fun getUserFromFirestore(userID: String) = firestore.collection("users").document(userID).get()


    /**
     *  getUserFromFirestore function is a suspend function that returns a Flow of User?.
     *  @param userID: String is the ID of the user whose data is to be fetched from Firestore.
     *  @return Flow<User?> is the Flow of User? that is emitted when the data is fetched from Firestore.
     */

    fun getUserFromFirestore(userID : String) : Flow<User?> = flow {
        try {
            val snapshot = withContext(Dispatchers.IO) {
                firestore.collection("users").document(userID).get().await()
            }
            val user = snapshot.toObject(User::class.java)
            // Log user
            Log.d("FirestoreRepository", " getUserFromFirestore User  : $user")

            emit(user)
        } catch (e: Exception) {
            // Log error or handle it as needed
            emit(null)
        }
    }


    fun updateUserInFirestore(userID: String, user: User) = firestore.collection("users").document(userID).update(
        mapOf(
            "userName" to user.userName,
            "email" to user.email,
            "profileImageUrl" to user.profileImageUrl,
            "name" to user.name
        )
    )

    fun deleteUserFromFirestore(userID: String) = firestore.collection("users").document(userID).delete()


    /**
     * Real Time Chat
     *
     */

    private val chatCollection = firestore.collection("chats")
    private val userCollection = firestore.collection("users")


    /**
     *  Send Message to Firestore
     *  @param chatMessage: ChatMessage is the message to be sent.
     */

    suspend fun sendMessage(chatMessage: ChatMessage){
        chatCollection.add(chatMessage).await()
    }

    /**
     * Get Chat Messages from Firestore
     */





    fun getMessages(user_id1: String, user_id2: String) : Flow<List<ChatMessage>> = callbackFlow {
        val query = chatCollection
            .orderBy("timestamp",Query.Direction.ASCENDING)
            .whereIn("sender_id", listOf(user_id1,user_id2))
            //.whereIn("receiver_id", listOf(user_id1,user_id2))

        val listener = query.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
            trySend(messages)
        }
        awaitClose{ listener.remove() }



    }



    /**
     *  Get chat partner from Firestore
     *  @param user_id: String is the ID of the user whose chat partner is to be fetched.
     *  @return User? is the User object of the chat partner.
     */

    suspend fun getChatPartner(user_id: String) : User?{
        return userCollection.document(user_id).get().await().toObject(User::class.java)

    }


    /**
     * Get last message by user ID
     */
    suspend fun getLastMessageByUserID(userID: String, currentUserID: String) : Flow<ChatMessage?> = flow {
        try {
            val snapshot = withContext(Dispatchers.IO) {
                chatCollection
                    .whereEqualTo("sender_id", userID)
                    .whereEqualTo("receiver_id", currentUserID)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()
            }
            val message = snapshot.toObjects(ChatMessage::class.java).firstOrNull()
            Log.d("FirestoreRepository", "getLastMessageByUserID Message: $message")
            emit(message)
        } catch (e: Exception) {
            emit(null)
            Log.e("FirestoreRepository", "getLastMessageByUserID Error: ${e.message}", e)
        }
    }


}