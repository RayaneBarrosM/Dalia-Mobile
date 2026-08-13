package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.SearchRequest
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: DaliaRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(SearchRequest(0, false, false, "", "", 0, 0))


    var isLoading by mutableStateOf(false)
       private set

    var searchSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun updateAge(age: Int) {
        _uiState.update { it.copy(age = age) }
    }
    fun updateCicloRegular(regularMenstruation: Boolean) {
        _uiState.update { it.copy(regularMenstruation = regularMenstruation) }
    }
    fun updateContraceptivo(useContraceptivo: Boolean) {
        _uiState.update { it.copy(useContraceptive= useContraceptivo) }
    }
    fun updateTypeContraceptive(contraceptiveType: String) {
        _uiState.update { it.copy(contraceptiveType = contraceptiveType) }
    }
    fun updateMenstruacaoDia(lastMenstruationDay: String) {
        _uiState.update { it.copy(lastMenstruationDay = lastMenstruationDay) }
    }
    fun updateCicleDuration(cicleDuration: Int) {
        _uiState.update { it.copy(cycleDuration = cicleDuration) }
    }

    fun atualizarDadosQuiz(campo: String, valor: Any) {
        when (campo) {
            "idade" -> updateAge(valor as Int)
            "cicloRegular" -> updateCicloRegular(valor as Boolean)
            "contraceptivo" -> updateContraceptivo(valor as Boolean)
            "tipoContraceptivo" -> updateTypeContraceptive(valor as String)
            "ultimaMenstruacao" -> updateMenstruacaoDia(valor as String)
            "duracaoCiclo" -> updateCicleDuration(valor as Int)
        }
    }

    fun onQuizFinish(){
        errorMessage = null
        isLoading = true
        viewModelScope.launch {
            try{
                val request = _uiState.value
                val response = repository.search(request)
                Log.d("TESTE", "Tentando salvar pesquisa")
                Log.d("TESTE", request.toString())

                if(response.isSuccess){
                    searchSuccess = true
                    Log.d("API_SUCESS","pequisa Salva")
                }else{
                    searchSuccess = false
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