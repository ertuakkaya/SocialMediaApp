package com.example.socialmediaapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.repository.PostRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {


    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    val dumyPost = Post(
        id = "id",
        userName = "firestore and firebase",
        profileImageUrl = "profileImageUrl",
        postImageUrl = "postImageUrl",
        postText = "post deneme ",
        timestamp = Timestamp.now(),
        likeCount = 44,
        commentCount = 0,
        likedBy = listOf(),
        comments = listOf(),


    )

    init {
        viewModelScope.launch {
            postRepository.getPostsFlow().collect {
                _posts.value = it
                //Log.d("PostViewModel ", "Posts: $it")
            }
        }

        //deleteAllPosts()
        //createPost(dumyPost)


    }



    fun createPost(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.createPost(post)
                // Optionally update the local list of posts

                // Optionally navigate back

                // Optionally show a success message

                // Optionally clear the text fields

                // Optionally refresh the posts

                // Upload images to firebase storage ///////




            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            try {
                postRepository.incrementLikeCount(postId)
                // Optionally update the local post object
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(postId)
                // Optionally update the local list of posts
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.deleteAllPosts()

                // update post list


            } catch (e: Exception) {
                // Handle error
            }
        }
    }

}