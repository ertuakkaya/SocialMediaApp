package com.example.socialmediaapp.util

data class ImageUploadResult(
    val success: Boolean,
    val imageUrl: String? = null,
    val errorMessage: String? = null
)
