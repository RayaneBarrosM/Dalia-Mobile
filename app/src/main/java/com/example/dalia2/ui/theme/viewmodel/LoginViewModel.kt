package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.ChangePassword
import com.example.dalia2.data.model.ForgetPassword
import com.example.dalia2.data.model.LoginRequest
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: DaliaRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginRequest("", ""))

    private val _emailState = MutableStateFlow(ForgetPassword(""))


    private val _changeState = MutableStateFlow(ChangePassword("",""))

    var isLoading by mutableStateOf(false)
        private set

    var loginSucess by mutableStateOf(false)
        private set

    var forgetSucess by mutableStateOf(false)
        private set

    var changeSucess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChanged(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onEmailForgetChanged(newEmail: String) {
        _emailState.update { it.copy(email = newEmail) }
    }

    fun onNewPasswordChanged(newPassword: String) {
        _changeState.update { it.copy(password = newPassword) }
    }
    fun onPassConfirmationChanged(newPassConfirmation: String) {
        _changeState.update { it.copy(passConfirmation = newPassConfirmation) }
    }

    fun onLoginClick(email: String, password: String) {
        errorMessage = null
        if (email.isEmpty()) {
            errorMessage = "Email é obrigatorio"
            return
        } else if (password.isEmpty()) {
            errorMessage = "Senha é obrigatoria"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _uiState.value.copy(email = email, password = password)
                val response = repository.login(request)
                Log.d("TESTE", "tentando logar")
                if (response.isSuccess) {
                    loginSucess = true
                    Log.d("API_SUCESS", "Usuario logado")
                } else {
                    loginSucess = false
                    errorMessage = repository.login(request).exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha ao fazer login"
            } finally {
                isLoading = false
            }
        }
    }

    fun onForgetPassClick(email: String) {
        errorMessage = null
        if (email.isEmpty()) {
            errorMessage = "Email é obrigatorio"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _emailState.value.copy(email = email)
                val response = repository.forgetPassword(request)
                Log.d("TESTE", "tentando forget")
                if (response.isSuccess) {
                    forgetSucess = true
                    Log.d("API_SUCESS_LOGIN", "Email recebido")
                } else {
                    forgetSucess = false
                    errorMessage = repository.forgetPassword(request).exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha ao click forget"
            } finally {
                isLoading = false
            }
        }
    }

    fun onChangePass(password: String, passConfirmation: String) {
        errorMessage = null
        if (password.isEmpty()) {
            errorMessage = "Senha é obrigatorio"
            return
        } else if (passConfirmation.isEmpty()){
            errorMessage = "Repita a senha"
            return
        }
        isLoading = true
        viewModelScope.launch {
            try {
                val request = _changeState.value.copy(password = password, passConfirmation = passConfirmation)
                val response = repository.changePass(request)
                Log.d("TESTE_LOGIN", "tentando mudar a senha")
                if (response.isSuccess) {
                    changeSucess = true
                    Log.d("API_SUCESS_LOGIN", "Senha mudada")
                } else {
                    changeSucess = false
                    errorMessage = repository.changePass(request).exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha ao trocar senha"
            } finally {
                isLoading = false
            }
        }
    }
}