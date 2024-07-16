package com.example.socialmediaapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.repository.PostRepository
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

    init {
        viewModelScope.launch {
            postRepository.getPostsFlow().collect {
                _posts.value = it
                //Log.d("PostViewModel ", "Posts: $it")
            }
        }
    }



    fun createPost(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.createPost(post)
                // Optionally update the local list of posts
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

}