package com.example.socialmediaapp.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.Comment
import com.example.socialmediaapp.data.entitiy.Like
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.example.socialmediaapp.data.repository.PostRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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


    /**
     * loadComments is loading comments
     * @param postID : String = "tDB9co8mVoIKm7hWbZHN"
     */
    fun loadComments(postID: String = "tDB9co8mVoIKm7hWbZHN"){
        viewModelScope.launch {
            try {
                val loadedComments = postRepository.getComments(postID)
                _comments.value = loadedComments

                Log.d("PostViewModel loadComment", "Comments: $loadedComments")
            } catch (e: Exception){
                // Exeption log
                Log.e("PostViewModel loadComment", "Error loading comments", e)
            }
        }
    }


    init {

        //getUser()

        //addComment()

        loadComments()


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
    fun getUser(userID: String = "GmxAlLex17gxyiux1sPegopWEYX2") {
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

}



data class PostState(
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
)

