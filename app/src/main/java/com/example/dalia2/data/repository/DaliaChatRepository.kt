package com.example.dalia2.data.repository

import com.example.dalia2.network.DaliaChatApiService
import com.example.dalia2.network.DaliaChatMessageRequest
import com.example.dalia2.network.DaliaChatRequest
import javax.inject.Inject

class DaliaChatRepository @Inject constructor(
    private val api: DaliaChatApiService
) {
    suspend fun sendMessage(
        messages: List<DaliaChatMessageRequest>
    ): String {
        val response = api.sendMessage(
            DaliaChatRequest(messages = messages)
        )

        return response.reply
    }
}
