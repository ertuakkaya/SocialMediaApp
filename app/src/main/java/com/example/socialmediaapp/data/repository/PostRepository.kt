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

    suspend fun incrementLikeCount(postId: String,like : Like,) {
        postsCollection.document(postId).update("likeCount", FieldValue.increment(1)).await()

        // add Like to likedBy
        postsCollection.document(postId)
            .collection("likedBy")
            .add(like).await()

    }

    suspend fun decrementLikeCount(postId: String) {
        postsCollection.document(postId).update("likeCount", FieldValue.increment(-1)).await()
    }

    suspend fun deleteAllPosts() {
        postsCollection.get().await().documents.forEach {
            it.reference.delete()
        }
    }

    suspend fun getLikesByUserId(): Boolean {
//        val deneme = postsCollection.document(post.id)
//        val snapshot = postsCollection.document().collection("likedBy").whereEqualTo("user", firebaseAuth.currentUser).get().await()
//        val snapshot = postsCollection.document("tDB9co8mVoIKm7hWbZHN").collection("likedBy").get().await()
//        val snapshot1 = postsCollection.document("/posts/tDB9co8mVoIKm7hWbZHN/likedBy/").get().await()
//        val like = snapshot1.toObject(Like::class.java)
//            .whereEqualTo("likedBy", firebaseAuth.currentUser!!)
////            .whereArrayContains("likedBy", firebaseAuth.currentUser!!)
//            .get()
//            .await()

        val deneme = postsCollection.whereArrayContains("likedBy", "/users/"+firebaseAuth.currentUser?.uid.toString()).get().await()

        val userList = deneme.toObjects(User::class.java)
//        Log.d("likedBy", snapshot.documents.toString())
//        Log.d("likedBy like ", like.toString())
        Log.d("likedBy deneme ", deneme.documents.toString())
        Log.d("likedBy userlist ", userList.size.toString())
//        return !snapshot.isEmpty

        return false
//        if (snapshot.isEmpty) {
//            return false
//        }else{
//            return true
//        }
    }
    ///////




//    // /posts/tDB9co8mVoIKm7hWbZHN
//   // posts document id = tDB9co8mVoIKm7hWbZHN
//    // current user id = GmxAlLex17gxyiux1sPegopWEYX2
//    fun LikeOrUnlike(postID: String, userId: String? = currentUSer?.uid) :Int {
//
//        val postRef = firestore.collection("posts").document("tDB9co8mVoIKm7hWbZHN")
//        var likeCount = 0
//
//        postRef.get()
//            .addOnSuccessListener { document ->
//                document.let { noNullDocument ->
//
//                    val currentArray = noNullDocument.get("likedBy") as? ArrayList<String> ?: ArrayList()
//
//                    Log.d("AddLike PostRepo", "Current Array: $currentArray")
//
//                    // check if the user is already in the list. it so unlike the post and delete the user from the list
//                    if (currentArray.contains("GmxAlLex17gxyiux1sPegopWEYX2")) { // if the user is already liked the post
//
//
//                        // unlike the post
//                        currentArray.remove("GmxAlLex17gxyiux1sPegopWEYX2")
//                        postRef.update("likedBy", currentArray)
//                            .addOnSuccessListener {
//                                Log.d(
//                                    "AddLike PostRepo",
//                                    "DocumentSnapshot removod with ID: ${noNullDocument.id}, likedBy: $currentArray"
//                                )
//                                Log.d("AddLike PostRepo", "User unliked this post")
//                            }
//                            .addOnFailureListener { e ->
//                                Log.w("AddLike PostRepo", "Error adding document", e)
//                            }
//                    } else { // if the user is not liked the post
//                        currentArray.add("GmxAlLex17gxyiux1sPegopWEYX2")
//
//                        postRef.update("likedBy", currentArray)
//
//                            .addOnSuccessListener {
//                                Log.d(
//                                    "AddLike PostRepo",
//                                    "DocumentSnapshot added with ID: ${noNullDocument.id}"
//                                )
//                            }
//
//                            .addOnFailureListener { e ->
//                                Log.w("AddLike PostRepo", "Error adding document", e)
//                            }
//
//                        Log.d("AddLike PostRepo", "User liked this post")
//                    }
////                    getLikeCount(currentArray).let {
////                        Log.d("AddLike PostRepo", "Like Count: $it")
////                    }
//                    likeCount = currentArray.size
//
//                }
//
//
//            }
//            .addOnFailureListener { e ->
//                Log.d("AddLike PostRepo", "Error getting documents.", e)
//            }
//
//        return likeCount
//    }
//    // /posts/tDB9co8mVoIKm7hWbZHN
//    // posts document id = tDB9co8mVoIKm7hWbZHN
//    // current user id = GmxAlLex17gxyiux1sPegopWEYX2
//    suspend fun CheckIfUserLikedThePost(postID: String, userId: String?) : Boolean {
//
//
//        return try {
//            val postRef = firestore.collection("posts").document("tDB9co8mVoIKm7hWbZHN")
//            val document = postRef.get().await()
//            val likedBy = document.get("likedBy") as? ArrayList<String> ?: ArrayList()
//            Log.d("CheckIfUserLikedThePost", "likedBy:  $likedBy")
//            likedBy.contains("GmxAlLex17gxyiux1sPegopWEYX2")
//
//        } catch (e: Exception) {
//            Log.e("checkIfUserLikedThePost", "Error checking if user liked post: ${e.message}", e)
//            false
//        }
//    }

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



    // /posts/tDB9co8mVoIKm7hWbZHN
    // posts document id = tDB9co8mVoIKm7hWbZHN
    // current user id = GmxAlLex17gxyiux1sPegopWEYX2
    fun getLikeCount( currentArray : ArrayList<String>) : Int {


        return currentArray.size
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


