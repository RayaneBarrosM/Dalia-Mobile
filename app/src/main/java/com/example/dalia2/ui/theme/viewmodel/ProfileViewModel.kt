package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.AppMode
import com.example.dalia2.data.model.PregnancyData
import com.example.dalia2.data.model.ProfileRequest
import com.example.dalia2.data.model.ProfileResponse
import com.example.dalia2.data.model.SearchData
import com.example.dalia2.data.repository.DaliaRepository
import com.example.dalia2.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SealedClassSerializer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: DaliaRepository
) : ViewModel() {

    var _uiState by mutableStateOf<ProfileResponse?>(null)

    var isLoading by mutableStateOf(false)
        private set
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _perfil = MutableStateFlow<ProfileResponse?>(null)
    val perfil: StateFlow<ProfileResponse?> = _perfil.asStateFlow()

    val currentMode: StateFlow<AppMode> = _perfil.map { profile ->
        profile?.currentMode ?: AppMode.MENSTRUACAO
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppMode.MENSTRUACAO
    )

    fun loadUserProfile(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (UserSession.profileCache == null || forceRefresh) {
                isLoading = true
                val response = repository.getUserFullProfile()
                if (response.isSuccess) {
                    UserSession.profileCache = response.getOrNull()
                    _uiState = UserSession.profileCache
                    _perfil.value = UserSession.profileCache
                }
                isLoading = false
            } else {
                _uiState = UserSession.profileCache
            }
        }
    }

    fun retornaMenstrucao(
        searchData: SearchData,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true

            val currentUser = _uiState?.user
            val currentPregnancy = _uiState?.pregnancyMonitoring

            val profileRequest = ProfileRequest(
                name = currentUser?.name,
                surname = currentUser?.surname,
                email = currentUser?.email,
                modo = "MENSTRUACAO",
                search = SearchData(
                    age = searchData.age,
                    useContraceptive = searchData.useContraceptive,
                    contraceptiveType = searchData.contraceptiveType
                ),
                pregnancyMonitoring = PregnancyData(
                    isPregnant = false,
                    startDate = "",
                    gestationWeeks = 0,
                    expectedBirthDate = ""
                )
            )
            val result = repository.updatePerfil(profileRequest)
            result.onSuccess { response ->
                UserSession.profileCache = null
                loadUserProfile(forceRefresh = true)
                onSuccess()
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            isLoading = false
        }
    }


    fun updateUserProfile(userRegistre : ProfileRequest, onSuccess: () ->Unit) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.updatePerfil(userRegistre)
            result.onSuccess { response ->
            _uiState = response
                onSuccess()
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            isLoading = false
        }
    }

    fun enviarDenuncia(mensagem: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            isLoading = true
            val body = RequestBody.create("text/plain".toMediaTypeOrNull(), mensagem)
            val result = repository.needHelp(body)
            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                Log.d("API_ERROR", "Erro enviar denuncia: ${error.message}")
                _errorMessage.value = error.message

            }
            isLoading = false
        }
    }
}
