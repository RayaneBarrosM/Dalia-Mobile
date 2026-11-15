package com.example.dalia2.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.repository.DaliaChatRepository
import com.example.dalia2.network.DaliaChatMessageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import androidx.compose.ui.text.input.TextFieldValue

data class DaliaChatMessage(
    val id: Long,
    val text: String,
    val fromUser: Boolean
)

data class DaliaChatUiState(
    val messages: List<DaliaChatMessage> = listOf(
        DaliaChatMessage(
            id = 0,
            text = "Oii! 💜 Eu sou a Dália. Pode conversar comigo, tirar dúvidas ou simplesmente compartilhar o que está acontecendo.",
            fromUser = false
        )
    ),
    val inputText: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DaliaChatViewModel @Inject constructor(
    private val repository: DaliaChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DaliaChatUiState())
    val uiState = _uiState.asStateFlow()

    private var nextMessageId = 1L

    fun onInputChanged(value: TextFieldValue) {
        val limitedValue = if (value.text.length <= 4000) {
            value
        } else {
            TextFieldValue(value.text.take(4000))
        }

        _uiState.update {
            it.copy(
                inputText = limitedValue,
                errorMessage = null
            )
        }
    }

    fun sendMessage() {
        val current = _uiState.value
        val text = current.inputText.text.trim()

        if (text.isBlank() || current.isLoading) return

        val userMessage = DaliaChatMessage(
            id = nextMessageId++,
            text = text,
            fromUser = true
        )

        val updatedMessages = current.messages + userMessage

        _uiState.update {
            it.copy(
                messages = updatedMessages,
                inputText = TextFieldValue(""),
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val conversation = updatedMessages
                    .takeLast(20)
                    .map { message ->
                        DaliaChatMessageRequest(
                            role = if (message.fromUser) "user" else "assistant",
                            content = message.text
                        )
                    }

                val reply = repository.sendMessage(conversation)

                _uiState.update { state ->
                    state.copy(
                        messages = state.messages + DaliaChatMessage(
                            id = nextMessageId++,
                            text = reply,
                            fromUser = false
                        ),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: IOException) {
                showError(
                    "Não consegui me conectar ao servidor. Confira se o backend está rodando e tente novamente."
                )
            } catch (e: HttpException) {
                showError(
                    "O servidor respondeu com um erro (${e.code()}). Tente novamente em alguns instantes."
                )
            } catch (e: Exception) {
                showError(
                    "Tive um probleminha ao responder. Tente novamente daqui a pouquinho."
                )
            }
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = message
            )
        }
    }
}
