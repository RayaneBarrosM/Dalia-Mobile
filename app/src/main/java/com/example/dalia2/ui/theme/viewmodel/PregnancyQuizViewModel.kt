package com.example.dalia2.ui.theme.viewmodel

import android.text.BoringLayout
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.PregnancyRequest
import com.example.dalia2.data.model.SearchRequest
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PregnancyQuizViewModel @Inject constructor(
    private val repository: DaliaRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(PregnancyRequest(true, 0, "", false, false, emptyList(), emptyList()))


    var isLoading by mutableStateOf(false)
       private set

    var quizSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun updateGravida(isPregnant: Boolean) {
        _uiState.update { it.copy(isPregnant = true) }
    }
    fun updateSemanas(gestationWeeks: Int) {
        _uiState.update { it.copy(gestationWeeks = gestationWeeks) }
    }
    fun updateNascBebe(expectedBirthDate: String) {
        _uiState.update { it.copy(expectedBirthDate= expectedBirthDate) }
    }
    fun updatePlanejado(plannedPregnancy: Boolean) {
        _uiState.update { it.copy(plannedPregnancy = plannedPregnancy) }
    }
    fun updateMedicamentos(takeMedicine: Boolean) {
        _uiState.update { it.copy(takeMedicine = takeMedicine) }
    }
    fun updateHabitos(habits: List<String>) {
        _uiState.update { it.copy(habits = habits) }
    }
    fun updateSistomas(symptoms: List<String>) {
        _uiState.update { it.copy(symptoms = symptoms) }
    }

    fun atualizarDadosQuiz(campo: String, valor: Any) {
        when (campo) {
            "qtdSemanas" -> updateSemanas(valor as Int)
            "previsaoNascimento" -> updateNascBebe(valor as String)
            "planejado" -> updatePlanejado(valor as Boolean)
            "remedios" -> updateMedicamentos(valor as Boolean)
            "habitos" -> updateHabitos(valor as List<String>)
            "sintomas" -> updateSistomas(valor as List<String>)
        }
    }

    fun onQuizFinish(){
        errorMessage = null
        isLoading = true
        viewModelScope.launch {
            try{
                val request = _uiState.value
                val response = repository.pregnancyQuiz(request)
                Log.d("TESTE", "Tentando salvar pesquisa gravidez")
                Log.d("TESTE", request.toString())

                if(response.isSuccess){
                    quizSuccess = true
                    Log.d("API_SUCESS","pequisa gravidez Salva")
                }else{
                    quizSuccess = false
                    errorMessage = response.exceptionOrNull()?.message
                }
            }catch(e: Exception){
                Log.d("API_ERROR", e.message.toString())
                errorMessage = "Falha ao salvar pesquisa"
            }finally {
                isLoading = false
            }
        }
    }
}