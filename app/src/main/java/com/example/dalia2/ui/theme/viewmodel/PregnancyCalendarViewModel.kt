package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.Weeks
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class PregnancyCalendarViewModel @Inject constructor(
    private val repository: DaliaRepository,
): ViewModel() {
    var eventSucess by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var _uiState by mutableStateOf<Weeks?>(null)
    private val _semana = MutableStateFlow<Weeks?>(null)
    val semana = _semana.asStateFlow()

    var semanaAtual by mutableStateOf(3)
        private set

    fun inciarDados(){
        viewModelScope.launch {
            val dataInicio = repository.getDataInicio()
            semanaAtual = calcularSemana(dataInicio)
            carregarSemana(semanaAtual)

        }
    }

    fun calcularSemana(dataInicio: String?): Int {
        if (dataInicio == null) return 1
        val dataInicio = LocalDate.parse(dataInicio)
        val diasPassados = ChronoUnit.DAYS.between(dataInicio, LocalDate.now())
        val semanas = (diasPassados / 7).toInt() + 1
        return semanas.coerceIn(1, 42)
    }
    fun carregarSemana(semana: Int){
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getWeek(semana)
                if (response.isSuccess) {
                    eventSucess = true
                    _semana.value = response.getOrThrow()
                } else {
                    eventSucess = false
                    errorMessage = response.exceptionOrNull()?.message ?: "Erro desconhecido"
                    Log.d("API_ERROR", "Error na resposta: $errorMessage")
                }
            } catch (e: Exception) {
                eventSucess = false
                errorMessage = "Falha na conexão"
                Log.d("API_ERROR", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
}