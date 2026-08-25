package com.example.dalia2.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ModeViewModel @Inject constructor() : ViewModel() {

    // Estado interno mutável (começa como false por padrão)
    private val _isPregnantMode = MutableStateFlow(false)

    // Estado público somente leitura para as telas/composables observarem
    val isPregnantMode: StateFlow<Boolean> = _isPregnantMode.asStateFlow()

    // Função para alterar o modo (ex: chamada ao finalizar o quiz de gravidez)
    fun setPregnantMode(isPregnant: Boolean) {
        _isPregnantMode.value = isPregnant
    }

    // Função para alternar o modo manualmente (toggle)
    fun togglePregnantMode() {
        _isPregnantMode.value = !_isPregnantMode.value
    }
}