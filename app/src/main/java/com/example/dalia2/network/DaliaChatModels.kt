package com.example.dalia2.network

data class DaliaChatMessageRequest(
    val role: String,
    val content: String
)

data class DaliaChatRequest(
    val messages: List<DaliaChatMessageRequest>
)

data class DaliaChatResponse(
    val reply: String
)
