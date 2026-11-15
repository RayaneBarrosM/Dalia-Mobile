package com.example.dalia2.network

import retrofit2.http.Body
import retrofit2.http.POST

interface DaliaChatApiService {
    @POST("chat")
    suspend fun sendMessage(
        @Body request: DaliaChatRequest
    ): DaliaChatResponse
}
