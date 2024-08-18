package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Comment
import com.example.socialmediaapp.data.entitiy.Like
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.example.socialmediaapp.data.repository.PostRepository
import com.example.socialmediaapp.util.ImageUploadResult
import com.example.socialmediaapp.util.PostState
import com.example.socialmediaapp.util.Result
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository,private val firestoreRepository: FirestoreRepository) : ViewModel() {


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
        _uploadStatus.value = ImageUploadResult(success = true, errorMessage = null, imageUrl = null)
        return postRepository.uploadPostImage(uri, fileName, imagePath)

    }

    fun resetUploadStatus() {
        _uploadStatus.value = null
    }

    // load post
    fun loadPosts() {
        viewModelScope.launch {
            postRepository.getPostsFlow().collect {
                _posts.value = it
            }

        }
    }

    fun observePostState(post_id: String) {
        viewModelScope.launch {
            postRepository.observePost(post_id).collect{post ->
                post.let {
                    updatePostState(it!!.id){
                        PostState(
                            isLiked = it.isLiked,
                            likeCount = it.likeCount,
                            commentCount = it.commentCount
                        )
                    }
                }

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


    // UI state'i tutmak için
    private val _uploadStatus = MutableStateFlow<ImageUploadResult?>(null)
    val uploadStatus: StateFlow<ImageUploadResult?> = _uploadStatus.asStateFlow()


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





    /**
     *      _postStates is getting postID and PostState
     *      PostState is getting isLiked, likeCount, commentCount
     *      postStates is getting postID and PostState
     *
     *
     *
     *      using :
     *      val postStates by postViewModel.postStates.collectAsState()
     *      val currentPostState = postStates[post.id] ?: PostState()
     *
     *      val isPostLiked = currentPostState.isLiked
     *      val likeCount = currentPostState.likeCount
     */

    private val _postStates = MutableStateFlow<Map<String, PostState>>(emptyMap())
    val postStates: StateFlow<Map<String, PostState>> = _postStates.asStateFlow()






    /**
     *    checkIfUserLikedThePost is getting postID and userID
     *    updatePostState is getting postID and update function
     *    update function is getting PostState and returning PostState
     *
     */
    fun checkIfUserLikedThePost(postId: String, userId: String) {
        viewModelScope.launch {
            val isLiked = postRepository.checkIfUserLikedThePost(postId, userId)
            updatePostState(postId) { it.copy(isLiked = isLiked) }
        }
    }


    /**
     *    likeOrUnlikePost is getting postID and userID
     *    updatePostState is getting postID and update function
     *    update function is getting PostState and returning PostState
     *
     */
    fun likeOrUnlikePost(postId: String, userId: String) {
        viewModelScope.launch {
            val (isLiked, likeCount) = postRepository.likeOrUnlike(postId, userId)


            /**
             *     updatePostState is getting postID and update function
             *     update function is getting PostState and returning PostState
             *     PostState is getting isLiked, likeCount, commentCount
             */
            updatePostState(postId) {
                it.copy(isLiked = isLiked, likeCount = likeCount)
            }


        }
    }




    /**
     *   updatePostState is getting postID and update function
     *   update function is getting PostState and returning PostState
     *   _postStates is returning postID and PostState
     */
    fun updatePostState(postId: String, update: (PostState) -> PostState) {
        _postStates.update { currentMap ->
            val currentState = currentMap[postId] ?: PostState()
            currentMap + (postId to update(currentState))
        }

    }




    /////// Comment Section

    /**
     *    test1 : ID  GmxAlLex17gxyiux1sPegopWEYX2
     *    last post ID : tDB9co8mVoIKm7hWbZHN
     */

        private val _comments = MutableStateFlow<List<Comment>>(emptyList())
        val comments: StateFlow<List<Comment>> = _comments.asStateFlow()





    // loadComment deneme sonu

    private val _commentState = MutableStateFlow <Result <List<Comment>>> (Result.Loading)
    val commentsState : StateFlow<Result<List<Comment>>> = _commentState.asStateFlow()

    // worked but bugged
    fun loadComments(postID : String){
        viewModelScope.launch {
            postRepository.getComments(postID = postID).collect{ result ->
                _commentState.value = result

            }
        }
    }








    init {

        //getUser()

        //addComment()

        //loadComments()


    }




    /**
     *   addComment is getting postID and comment
     *   @param postID : String = "tDB9co8mVoIKm7hWbZHN"
     *   @param comment : String = "deneme1"
     *
     *
     *
     */
    fun addComment(postID: String = "tDB9co8mVoIKm7hWbZHN", comment: String = "deneme1",user : User = User()) {

        viewModelScope.launch {
            try {
                val newComment = postRepository.addComment(postID, comment, user)
                _comments.value = _comments.value + newComment
                _commentState.value = Result.Success(_comments.value) ///////////
                updatePostState(postID){
                    it.copy(commentCount = it.commentCount + 1)

                }

            } catch (e: Exception){
                // Exeption log
                Log.e("PostViewModel addComment", " Error adding comment", e)
            }
        }
    }

    // user flow is getting user from firestore
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    /**
     * getUser is getting currently logged in user from firestore
     * @param userID : String
     * @return User
     */
    suspend fun getUser(userID: String = "GmxAlLex17gxyiux1sPegopWEYX2") {
        //var user = User()
        Log.d("PostViewModel getUser", "getUser is called")
        viewModelScope.launch {
            try {
                firestoreRepository.getUserFromFirestore(userID).collect{user ->
                    _user.value = user
                    Log.d("PostViewModel getUser", "_user.value: ${_user.value}")

                }
            } catch (e: Exception){
                // Exeption log
                Log.e("PostViewModel getUser", " Error getting user", e)

            }
        }

    }


    private val _lastComment = MutableStateFlow<Comment?>(null)
    val lastComment: StateFlow<Comment?> get() = _lastComment.asStateFlow()

    fun fetchLastComment(postID : String) {
        viewModelScope.launch {
            try {
                val comment = postRepository.fetchLastComment(postID = postID)
                _lastComment.value = comment

            } catch (e: Exception) {
                Log.d("PostViewModel", "fetchLastComment: $e")
            }
        }
    }

    private val _commentCount = MutableStateFlow<Int?>(0)
    val commentCount: StateFlow<Int?> get() = _commentCount.asStateFlow()


    fun fetchCommentCount(post_id : String){
        viewModelScope.launch {
            try {
                val commentCount = postRepository.fetchCommentCount(post_id)
                _commentCount.value = commentCount
            } catch (e: Exception) {
                Log.d("PostViewModel", "fetchCommentCount: $e")
            }
        }
    }

}




