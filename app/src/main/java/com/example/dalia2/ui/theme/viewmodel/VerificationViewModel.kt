package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.VerificationRequest
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val repository: DaliaRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(VerificationRequest("", ""))
    var isLoading by mutableStateOf(false)
        private set
    var verificationSucess by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onTokenChanged(newToken: String) {
        _uiState.update { it.copy(token = newToken) }
    }

    fun onVerifyClick(email: String, token: String) {
        errorMessage = null
        if(email.isEmpty()){
            errorMessage = "Email não encontrado"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _uiState.value.copy(email=email, token = token)
                val response = repository.verifiyCode(request)
                Log.d("TESTE", "tenteando verificar. email: $email e token $token")
                if (response.isSuccess) {
                    verificationSucess = true
                    Log.d("API_SUCESS", "Usuario verificado")
                } else {
                    verificationSucess = false
                    errorMessage = repository.verifiyCode(request).exceptionOrNull()?.message
                }
            } catch (e: Exception){
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha na verificação"
            } finally {
                isLoading = false
            }
        }
    }

    fun onVerifyToken(email: String, token: String) {
        errorMessage = null
        if(email.isEmpty()){
            errorMessage = "Email não encontrado"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _uiState.value.copy(email=email, token = token)
                val response = repository.verifiyToken(request)
                Log.d("TESTE", "tenteando verificar. email: $email e token $token")
                if (response.isSuccess) {
                    verificationSucess = true
                    Log.d("API_SUCESS", "Usuario verificado")
                } else {
                    verificationSucess = false
                    errorMessage = repository.verifiyToken(request).exceptionOrNull()?.message
                }
            } catch (e: Exception){
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha na verificação"
            } finally {
                isLoading = false
            }
        }
    }

    fun resetState() {
        verificationSucess = false
        errorMessage = null
    }
}