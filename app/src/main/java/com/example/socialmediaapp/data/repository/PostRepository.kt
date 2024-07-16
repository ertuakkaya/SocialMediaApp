package com.example.socialmediaapp.data.repository

import com.example.socialmediaapp.data.entitiy.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepository @Inject constructor(private val firestore : FirebaseFirestore) {

    private val postsCollection = firestore.collection("posts")

    suspend fun createPost(post: Post): String {
        val newPostRef = postsCollection.document()
        val postWithId = post.copy(id = newPostRef.id)
        newPostRef.set(postWithId).await()
        return newPostRef.id
    }


    // get post by id
    fun getPostsFlow(): Flow<List<Post>> = flow {
        val snapshot = postsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()
        val posts = snapshot.toObjects(Post::class.java)
        emit(posts)
    }

    suspend fun getPostById(postId: String): Post? {
        val document = postsCollection.document(postId).get().await()
        return document.toObject(Post::class.java)
    }

    suspend fun updatePost(postId: String, updates: Map<String, Any>) {
        postsCollection.document(postId).update(updates).await()
    }

    suspend fun deletePost(postId: String) {
        postsCollection.document(postId).delete().await()
    }

    suspend fun incrementCommentCount(postId: String) {
        postsCollection.document(postId).update("commentCount", FieldValue.increment(1)).await()
    }

    suspend fun incrementLikeCount(postId: String) {
        postsCollection.document(postId).update("likeCount", FieldValue.increment(1)).await()
    }

    suspend fun decrementLikeCount(postId: String) {
        postsCollection.document(postId).update("likeCount", FieldValue.increment(-1)).await()
    }

    suspend fun deleteAllPosts() {
        postsCollection.get().await().documents.forEach {
            it.reference.delete()
        }
    }

}