package com.example.test.repository.model

data class MessageResponse(
    val user: String,
    val message: List<MessageItem>
)