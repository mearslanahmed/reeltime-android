package com.arslan.reeltime.model

/**
 * Centralized data class for a User.
 * Using default empty strings to make it robust for Firebase deserialization.
 */
data class User(
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = ""
)
