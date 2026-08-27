package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.CycleData
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: DaliaRepository,
) : ViewModel() {
    var registroSucesso by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf<Boolean?>(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var cycleData by mutableStateOf<CycleData?>(null)
        private set

    private var diasOvulacaoAcumulados = listOf<LocalDate>()


    private val _diasMenstruacao = MutableStateFlow<List<LocalDate>>(emptyList())
    val diasMenstruacao = _diasMenstruacao.asStateFlow()

    private val _diasFertil = MutableStateFlow<List<LocalDate>>(emptyList())
    val diasFertil = _diasFertil.asStateFlow()

    private val _diaOvulacao = MutableStateFlow<LocalDate?>(null)
    val diaOvulacao = _diaOvulacao.asStateFlow()


    init{
        carregarStatusHoje()
    }

    fun carregarStatusHoje() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getCiclo()
                if (response.isSuccess) {
                    registroSucesso = true
                    val dados = response.getOrNull()
                    listarDatas(dados)
                } else {
                    registroSucesso = false
                }
                errorMessage = repository.getCiclo().exceptionOrNull()?.message
                Log.d("API_ERROR", "Erro: ")
            } catch (e: Exception) {
                errorMessage = "Falha na conexão"
                Log.d("API_ERROR", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
    fun registrarMenstruacao() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.registrarMenstruacao()
                if (response.isSuccess) {
                    val dados = response.getOrNull()
                    listarDatas(dados)
                }
                errorMessage = repository.registrarMenstruacao().exceptionOrNull()?.message
                Log.d("API_ERROR", "Erro: ")
            } catch (e: Exception) {
                errorMessage = "Falha na conexão"
                Log.d("API_ERROR", e.message.toString())
            }finally {
                isLoading = false
            }
        }
    }

    private fun listarDatas(cycleData: CycleData?) {
        if (cycleData == null) return

        val listMenstruacao = mutableListOf<LocalDate>()
        val listFertil = mutableListOf<LocalDate>()
        val listOvulacao = mutableListOf<LocalDate>()

        val duracaoCiclo = if (cycleData.maxCycleDuration > 0) cycleData.maxCycleDuration else 28


        for (i in 0..2) {
            val diasAAdicionar = (duracaoCiclo * i).toLong()

            val inicioM = cycleData.lastMenstruationDay.plusDays(diasAAdicionar)
            var fimM = cycleData.fimMenstruacao.plusDays(diasAAdicionar)

            if (fimM.isBefore(inicioM)) {
                fimM = inicioM.plusDays(4)
            }

            val inicioF = cycleData.inicioPeriodoFertil.plusDays(diasAAdicionar)
            val fimF = cycleData.fimPeriodoFertil.plusDays(diasAAdicionar)
            val diaO = cycleData.diaOvulacao?.plusDays(diasAAdicionar)

            listMenstruacao.addAll(gerarListaDatas(inicioM, fimM))
            listFertil.addAll(gerarListaDatas(inicioF, fimF))
            if (diaO != null) {
                listOvulacao.add(diaO)
            }
        }

        // Atualiza as Threads seguras da UI com os 3 meses calculados!
        _diasMenstruacao.value = listMenstruacao
        _diasFertil.value = listFertil

        _diaOvulacao.value = cycleData.diaOvulacao

        // Salvamos a lista completa de ovulações estendida em uma variável interna para o clique do dia funcionar
        diasOvulacaoAcumulados = listOvulacao
    }


    fun getStatusDoDia(data: LocalDate): String {
        return when {
            _diasMenstruacao.value.contains(data) -> "MENSTRUAÇÃO"
            diasOvulacaoAcumulados.contains(data) -> "OVULAÇÃO"
            _diasFertil.value.contains(data) -> "PERIODO FERTIL"
            else -> ""
        }
    }

    private fun gerarListaDatas(inicio: LocalDate, fim: LocalDate): List<LocalDate> {
        val datas = mutableListOf<LocalDate>()
        var atual = inicio
        while (!atual.isAfter(fim)) {
            datas.add(atual)
            atual = atual.plusDays(1)
        }
        return datas
    }
}