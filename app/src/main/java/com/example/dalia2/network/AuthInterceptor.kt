package com.example.dalia2.network

import android.util.Log
import com.example.dalia2.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val sessionEvenBus: SessionEvenBus
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response{
        Log.d("AUTH_INTERCEPTOR", "Interceptando a requisição")
        val originRequest = chain.request()
        val token = sessionManager.getAccessToken()
        val request = if (!token.isNullOrBlank()) {
            originRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }else{
            originRequest
        }

        val response = chain.proceed(request)

        if(response.code == 401 || response.code == 403){
            sessionManager.clearSession()
            sessionEvenBus.notificaSessaoExpirada()
        }
        return response
    }
}