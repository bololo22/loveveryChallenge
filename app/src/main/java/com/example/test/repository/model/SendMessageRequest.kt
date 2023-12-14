package com.example.test.repository.model

data class SendMessageRequest(
    val user: String,
    val operation: String,
    val subject: String,
    val message: String
)