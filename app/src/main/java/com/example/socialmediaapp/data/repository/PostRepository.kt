package com.example.socialmediaapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.socialmediaapp.data.entitiy.Comment
import com.example.socialmediaapp.data.entitiy.Like
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.util.uploadFile
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.socialmediaapp.util.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class PostRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth : FirebaseAuth
) {

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


    ////// try
    fun getPost(post_id: String) : Task<DocumentSnapshot>{
        return firestore.collection("posts").document(post_id).get()
    }

    fun observePost(post_id: String): Flow<Post?> = callbackFlow {
        val postRef = firestore.collection("posts").document(post_id)

        val listenerRegistration = postRef.addSnapshotListener { snapshot, error ->
            if (error != null){
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()){
               val post = snapshot.toObject(Post::class.java)
                trySend(post)
            } else {
                trySend(null)
            }
        }
        awaitClose{
            listenerRegistration.remove()
        }
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





    suspend fun deleteAllPosts() {
        postsCollection.get().await().documents.forEach {
            it.reference.delete()
        }
    }

    suspend fun getLikesByUserId(): Boolean {
        val deneme = postsCollection.whereArrayContains("likedBy", "/users/"+firebaseAuth.currentUser?.uid.toString()).get().await()

        val userList = deneme.toObjects(User::class.java)

        Log.d("likedBy deneme ", deneme.documents.toString())
        Log.d("likedBy userlist ", userList.size.toString())
        return false
    }







    suspend fun likeOrUnlike(postId: String, userId: String): Pair<Boolean, Int> = withContext(
        Dispatchers.IO) {
        val postRef = firestore.collection("posts").document(postId)

        try {
            val document = postRef.get().await()
            val currentArray = document.get("likedBy") as? ArrayList<String> ?: ArrayList()

            val isLiked = if (currentArray.contains(userId)) {
                currentArray.remove(userId)
                false
            } else {
                currentArray.add(userId)
                true
            }

            postRef.update("likedBy", currentArray).await()

            /**
             * Return a Pair of two values:
             * isLiked and the updated like count
             *
             */
            Pair(isLiked, currentArray.size)


        } catch (e: Exception) {
            Log.e("LikeOrUnlike", "Error updating like status: ${e.message}", e)
            Pair(false, 0)
        }
    }

    suspend fun checkIfUserLikedThePost(postId: String, userId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val postRef = firestore.collection("posts").document(postId)
            val document = postRef.get().await()
            val likedBy = document.get("likedBy") as? ArrayList<String> ?: ArrayList()
            Log.d("CheckIfUserLikedThePost", "likedBy: $likedBy")
            likedBy.contains(userId)
        } catch (e: Exception) {
            Log.e("CheckIfUserLikedThePost", "Error checking if user liked post: ${e.message}", e)
            false
        }
    }


    ////// Comment Section


    /**
     * Load comments for a post
     * @param postID: The ID of the post to load comments for
     * @return  Comment: A list of comments for the post
     *
     */





    suspend fun getComments(postID: String) : Flow<Result<List<Comment>>> = flow {
        emit(Result.Loading)
        try {
            val commentsRef = firestore.collection("posts").document(postID).collection("comments")
            val snapshot = commentsRef.orderBy("createdAt", Query.Direction.DESCENDING).get().await()


            if (snapshot.isEmpty) {
                emit(Result.Failure(Exception("No comments found")))
            } else {
                val comments = snapshot.toObjects(Comment::class.java)
                //emit(Result.Success(comments))
                comments?.let {
                    emit(Result.Success(it))
                }
            }
            //emit(Result.Success(comments))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }

    }


    suspend fun fetchLastComment(postID: String) : Comment? = withContext(Dispatchers.IO) {
        try {
            val commentsRef = firestore.collection("posts").document(postID).collection("comments")
            val snapshot = commentsRef.orderBy("createdAt", Query.Direction.DESCENDING).limit(1).get().await()
            val lastComment = snapshot.toObjects(Comment::class.java).firstOrNull()
            lastComment
        } catch (e: Exception) {
            Log.e("FetchLastComment", "Error fetching last comment: ${e.message}", e)
            null
        }
    }




    /**
     * Add a comment to a post
     * @param postID: The ID of the post to add the comment to
     * @param commentText: The text of the comment
     * @return The added comment
     */

    suspend fun addComment(postID: String, commentText: String, user : User ) : Comment = withContext(Dispatchers.IO){

        val postRef = firestore.collection("posts").document(postID)
        val commentsRef = postRef.collection("comments")
        //val currentUser = FirebaseAuth.getInstance().currentUser //
        val currentUser = firebaseAuth.currentUser

        val newComment = Comment(
            commentID = commentsRef.document().id,
            commentText = commentText,
            createdAt = Timestamp.now(),
            userID = user.userID ?: "null",
            userName = user.userName ?: "null",
            profileImageUrl = user.profileImageUrl ?: "null"
        )

        val docRef = commentsRef.add(newComment).await()
        postRef.update("commentCount", FieldValue.increment(1)).await()

        Log.d("addComment Repo", "Comment added with ID: ${docRef.id}")


        // Return the added comment with the generated ID
        newComment.copy(commentID = docRef.id)


    }












    val currentUSer = firebaseAuth.currentUser
    val userId = currentUSer?.uid

    // unique  post id



    var post = Post(
        id  = "postDuzenleYeni",
        userId = "",
        userName = "",
        profileImageUrl = "",
        postImageUrl = "",
        postText  = "",
        commentCount  = 0,
        likeCount  = 0,
        timestamp = Timestamp.now(),
        likedBy  = emptyList(),
        comments  = emptyList()
    )




    fun postEkle(){
        firestore.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d("Post Repo Post Ekle ", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Post Repo Post Ekle ", "Error adding document", e)
            }


//        val user = firestore.collection("posts").document("ada")
//        Log.d("TAG", "postEkle: ${user.get()}")
    }


    fun postOku(){


        firestore.collection("posts")
            .get()
            .addOnSuccessListener { posts ->
                for (post in posts){
                    Log.d("Post Repo Post Oku", "postOku: ${post.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Post Repo Post Oku" , "Error getting documents.", e)
            }

    }


    fun postDuzenle(){

        firestore.collection("posts").document(userId!!)
            .set(post)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }



    }






    fun postDuzenle2(){
       post = Post(
           id  = "postDuzenleYeni",
           userId = userId!!,
           userName = "",
           profileImageUrl = "",
           postImageUrl = "",
           postText  = "post Text 2",
           commentCount  = 0,
           likeCount  = 0,
           timestamp = Timestamp.now(),
           likedBy  = emptyList(),
           comments  = emptyList()
       )


        firestore.collection("posts")
            .document(userId!!)
            .set(post , SetOptions.merge())
    }


    fun postAlaniniDuzenle(){


        val postRef = firestore.collection("posts").document(userId!!)

        postRef
            .update("postText", "post Text 3")
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }


    // update multiple fields
    fun postAlaniniDuzenle2(){


        firestore.collection("posts")
            .document(userId!!)
            .update(
                mapOf(
                    "postText" to "post Text 4",
                    "likeCount" to 1,
                    "userName" to "ertu123"
                )
            )
    }


    // increment like count
    fun postAlaniniDuzenle3(){


        firestore.collection("posts")
            .document(userId!!)
            .update(
                "likeCount" , FieldValue.increment(1)
            )
    }



    // get a documnet
    fun postOkuma(){


        firestore.collection("posts")
            .document(userId!!)
            .get()
            .addOnSuccessListener { document ->
                document.toObject(Post::class.java)?.let { post ->
                    Log.d("Okunan Post postText : ", "${post.postText}")
                }?: Log.d("Okunan Post postText :", "No such document")
            }
            .addOnFailureListener { e ->
                Log.d("Post Okuma" , "Error getting documents.", e)
            }

    }


    // okuma kaynagi secme : cache, server, default
    fun okumaKaynagi()
    {


        firestore.collection("posts")
            .document(userId!!)
            .get(Source.CACHE)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val document = task.result
                    document.toObject(Post::class.java)?.let { post ->
                        Log.d("okumaKaynagi CACHE postText : ", "${post.postText}")
                    }
                } else {
                    Log.d("okumaKaynagi", "Cached get failed: ", task.exception)
                }


            }


    }

    // custom object
    fun objeVerisiOku(){


        firestore.collection("posts").document(userId!!)
            .get()
            .addOnSuccessListener { document ->
                val gelenPost = document.toObject<Post>()
                Log.d("objeVerisiOku ", ": ${gelenPost}")
            }
            .addOnFailureListener { e ->
                Log.d("objeVerisiOku", "Error getting documents.", e)
            }

    }

    // https://firebase.google.com/docs/firestore/query-data/get-data#get_multiple_documents_from_a_collection
    // birden fazla document okuma
    // id si ada olan documentları oku
    fun cokluDocumentOku() {


        firestore.collection("posts")
            .whereEqualTo("id", "ada")
            .get()
            .addOnSuccessListener { posts ->
                for(post in posts){
                    Log.d("cokluDocumentOku", "cokluDocumentOku: ${post.data}")

                }
            }
            .addOnFailureListener { e ->
                Log.w("cokluDocumentOku", "Error getting documents.", e)
            }
    }


    val comment = Comment(
        commentID = "commentId",
        commentText = "commentText",
        createdAt = Timestamp.now(),
        userID = "",
        userName = "ertuakkaya",
        profileImageUrl = ""
    )

    // alt dokuman ekleme
    fun altDokumanEkle(){
        firestore.collection("posts")
            .document(userId!!)
            .collection("comments")
            .add(comment)
    }


    //Get all documents in a subcollection
    fun altDokumanlarıGetir(){


        firestore.collection("posts")
            .document(userId!!)
            .collection("comments")
            .get()
            .addOnSuccessListener { comments ->
                for(comment in comments){
                    Log.d("altDokumanlarıGetir", "altDokumanlarıGetir: ${comment.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.w("altDokumanlarıGetir", "Error getting documents.", e)
            }
    }


    // Get realtime updates with Cloud Firestore
    fun gercekZamanliGuncelleme()
    {


        firestore.collection("posts")
            .document(userId!!)
            .addSnapshotListener { value, error ->
                if(error != null){
                    Log.w("gercekZamanliGuncelleme" , "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (value != null && value.exists()) {
                    Log.d("gercekZamanliGuncelleme", "Current data: ${value.data}")
                } else {
                    Log.d("gercekZamanliGuncelleme", "Current data: null")
                }

            }
    }




   /////////////////////////////////////////////////////// Firebase Storage

    val storage = Firebase.storage

    // Create a storage reference from our app




    fun getbucket()
    {

        // Image ref
        var postImages : StorageReference? = firebaseStorage.reference.child("post_images")

        var image = postImages?.child("5711f637-ea89-41c3-bd39-63c78a43b83e")


        Log.d("getbucket", "postImages.path : ${postImages?.path}")
       Log.d("getbucket", "postImages.name : ${postImages?.name}")



        Log.d("getbucket", "image.path : ${image?.path}")
        Log.d("getbucket", "image.name : ${image?.name}")


        val mountainImagesRef = firebaseStorage.reference.child("post_images/5711f637-ea89-41c3-bd39-63c78a43b83e")

    }




    //////////////////////////////////////////////////////////////
    //https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_local_file
    // uploload from locam file
    suspend fun uploadPostImage(uri : Uri, fileName : String, imagePath : String) : Uri {


        return uploadFile(uri, fileName, imagePath)


    }






    // get url
    fun getUrl(fileName: String) : String {


        return fileName
    }

    suspend fun fetchCommentCount(post_id: String): Int {

        val commentsRef = firestore.collection("posts").document(post_id).collection("comments")
        val snapshot = commentsRef.get().await()
        return try {
            snapshot.size()
        } catch (e: Exception) {
            Log.e("FetchCommentCount", "Error fetching comment count: ${e.message}", e)
            0
        }

    }


    init {
        //postEkle()
        //postOku()
        //postDuzenle2()
        //postOkuma()
        //okumaKaynagi()
        //objeVerisiOku()
        //cokluDocumentOku()

        //altDokumanlarıGetir()

        //gercekZamanliGuncelleme()

        //getbucket()
    }

}




