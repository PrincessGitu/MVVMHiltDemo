package com.example.noteapp.model

data class NeoUserResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val user_msg: String
)