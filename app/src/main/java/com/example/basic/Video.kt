package com.example.basic

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val views: Int,
    val likes: Int,
    val channel_name : String,
)
