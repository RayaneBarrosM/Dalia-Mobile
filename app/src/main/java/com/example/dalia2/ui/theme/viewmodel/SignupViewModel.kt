package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.UserRegistre
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: DaliaRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(UserRegistre("", "", "", "", ""))
    var isLoading by mutableStateOf(false)
        private set
    var sigupSucess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set
    private val _isSuccess = MutableStateFlow(false)

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }
    fun onSurnameChanged(newSurname: String) {
        _uiState.update { it.copy(surname = newSurname) }
    }
    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }
    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }
    fun onPassConfirmationChanged(newPassConfirmation: String) {
        _uiState.update { it.copy(passConfirmation = newPassConfirmation) }
    }

    fun onRegistreClick(password:String, passConfirmation: String) {
        errorMessage = null

        if (password != passConfirmation) {
            errorMessage = "as senhas não conicidem"
            return
        }
        if (password.length < 8) {
            errorMessage = "Senha não segue os requisitos"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _uiState.value
                val response = repository.createUser(request)
                Log.d("TESTE", "Tentando criar")

                if (response.isSuccess) {
                    sigupSucess = true
                    Log.d("API_SUCESS", "Usuario: ${request.name} criado com sucesso")
                } else {
                    _isSuccess.value = false
                    errorMessage = repository.createUser(request).exceptionOrNull()?.message
                    Log.d("API_ERROR", "Erro: ")
                }
            } catch (e: Exception) {
                errorMessage = "Falha na conexão"
                Log.d("API_ERROR", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
    fun resetState() {
        sigupSucess = false
        errorMessage = null
    }
}