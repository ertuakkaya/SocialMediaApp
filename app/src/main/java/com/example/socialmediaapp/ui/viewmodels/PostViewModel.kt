package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Like
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.repository.PostRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
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

        loadPosts()


    }


    fun generatePostID() : String {
        var savedPostID: String? = null
        savedPostID = UUID.randomUUID().toString()
        if(savedPostID != null) {
            return savedPostID
        }
        return "null"
    }

    suspend fun uploadFile(uri : Uri, fileName : String, imagePath : String) : Uri? {
        return postRepository.uploadPostImage(uri, fileName, imagePath)
    }

    // load post
    fun loadPosts() {
        viewModelScope.launch {
            postRepository.getPostsFlow().collect {
                _posts.value = it
                //Log.d("PostViewModel ", "Posts: $it")
            }
        }
    }


    fun GetLikesByUserID() : Boolean{
        var result = false
        viewModelScope.launch {
            try {
//                result = postRepository.getLikesByUserId()
                postRepository.getLikesByUserId()
            } catch (e: Exception) {
                Log.d("PostViewModel", "GetLikesByUserID: $e")
            }
        }
        return result
    }



    fun createPost(post: Post) {
        viewModelScope.launch {
            try {
                postRepository.createPost(post).also {
                    Log.d("PostViewModel", "createPost Id : $it")
                }




            } catch (e: Exception) {
                // Handle error
            }
        }
    }






    fun likePost(postId: String, like : Like) {
        viewModelScope.launch {
            try {
                postRepository.incrementLikeCount(postId,like)
                // Optionally update the local post object
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun unlikePost(postId: String) {
        viewModelScope.launch {
            try {
                postRepository.decrementLikeCount(postId)
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

    fun getPostImageUrl(fileName: String) {
        viewModelScope.launch {
            try {
                postRepository.getUrl(fileName)
            } catch (e: Exception) {
                Log.d("PostViewModel", "getPostImageUrl: $e")
            }
        }
    }

    init {
        //LikeOrUnlike("1","1")
        CheckIfUserLikedThePost("1","1")


    }

    fun LikeOrUnlike(postID : String, userID : String) : Int {
        var likeCount = 0
        viewModelScope.launch {
            try {
                likeCount =  postRepository.LikeOrUnlike(postID,userID)
            } catch (e: Exception) {
                Log.d("PostViewModel", "likeOrUnlike: $e")
            }
        }
        return likeCount
    }

//    fun CheckIfUserLikedThePost(postId: String , userID: String) : Boolean
//    {
//        var result = false
//        viewModelScope.launch {
//            try {
//                result = postRepository.CheckIfUserLikedThePost(postId,userID)
//            } catch (e: Exception) {
//                Log.d("PostViewModel", "CheckIfUserLikedThePost: $e")
//            }
//        }
//        Log.d("PostViewModel CheckIfUserLikedThePost", "CheckIfUserLikedThePost: $result")
//        return result
//    }

    private val _isPostLiked = MutableStateFlow<Boolean?>(null)
    val isPostLiked: StateFlow<Boolean?> = _isPostLiked.asStateFlow()

    fun CheckIfUserLikedThePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                val result = postRepository.CheckIfUserLikedThePost(postId, userId)
                _isPostLiked.value = result
                Log.d("PostViewModel", "checkIfUserLikedThePost: $result")

            } catch (e: Exception) {
                Log.d("PostViewModel", "checkIfUserLikedThePost: $e")
                _isPostLiked.value = false
            }
        }
    }

//    fun getLikeCount(){
//        viewModelScope.launch {
//            try {
//                postRepository.getLikeCount()
//            } catch (e: Exception) {
//                Log.d("PostViewModel", "getLikeCount: $e")
//            }
//        }
//    }

}