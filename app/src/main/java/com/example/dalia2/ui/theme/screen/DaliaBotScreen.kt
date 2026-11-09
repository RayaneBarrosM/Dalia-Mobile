package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.viewmodel.DaliaChatMessage
import com.example.dalia2.ui.theme.viewmodel.DaliaChatUiState
import com.example.dalia2.ui.theme.viewmodel.DaliaChatViewModel
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

private val DaliaPink = Color(0xFFF18FB8)
private val DaliaPurple = Color(0xFFD286C9)
private val DaliaLight = Color(0xFFFFE8F1)
private val DaliaYellow = Color(0xFFFFF3B0)
private val DaliaText = Color(0xFF45313F)

@Composable
fun DaliaBotScreen(
    viewModel: DaliaChatViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    DaliaBotScreenContent(
        state = state,
        onInputChanged = viewModel::onInputChanged,
        onSendMessage = viewModel::sendMessage
    )
}

@Composable
private fun DaliaBotScreenContent(
    state: DaliaChatUiState,
    onInputChanged: (TextFieldValue) -> Unit,
    onSendMessage: () -> Unit
) {
    var showingHistory by rememberSaveable { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.messages.size, state.isLoading) {
        val lastIndex = state.messages.lastIndex +
                if (state.isLoading) 1 else 0

        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DaliaPurple,
                        Color(0xFFE99BCF),
                        Color(0xFFFFB3CA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // CABEÇALHO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.flower_nav),
                    contentDescription = "Flor da Dália",
                    modifier = Modifier.size(42.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dália",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = DaliaPink
                    )

                    Text(
                        text = "Sua companheira virtual 💜",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                TextButton(
                    onClick = {
                        showingHistory = !showingHistory
                    }
                ) {
                    Text(
                        text = if (showingHistory) "Voltar" else "Histórico",
                        color = DaliaPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (showingHistory) {
                HistoryContent(
                    onBack = { showingHistory = false }
                )
            } else {
                // ÁREA DE CONVERSA
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.94f))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.messages, key = { it.id }) { message ->
                        MessageBubble(message)
                    }

                    if (state.isLoading) {
                        item(key = "dalia_loading") {
                            LoadingBubble()
                        }
                    }
                }

                state.errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                            .padding(12.dp),
                        color = Color(0xFF9B3155),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // CAMPO DE MENSAGEM
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = onInputChanged,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 9.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = DaliaText,
                            fontSize = 14.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = { onSendMessage() }
                        ),
                        maxLines = 4,
                        decorationBox = { innerTextField ->
                            Box {
                                if (state.inputText.text.isEmpty()) {
                                    Text(
                                        text = "Converse com a Dália...",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onSendMessage,
                        enabled = state.inputText.text.isNotBlank() &&
                                !state.isLoading,
                        shape = CircleShape,
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaliaPink,
                            disabledContainerColor = DaliaPink.copy(alpha = 0.4f)
                        )
                    ) {
                        Text("➤", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: DaliaChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (message.fromUser) 18.dp else 4.dp,
                        bottomEnd = if (message.fromUser) 4.dp else 18.dp
                    )
                )
                .background(
                    if (message.fromUser) DaliaYellow else DaliaLight
                )
                .padding(horizontal = 15.dp, vertical = 11.dp)
        ) {
            Text(
                text = message.text,
                color = DaliaText,
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun LoadingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(DaliaLight)
                .padding(horizontal = 15.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = DaliaPurple,
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.width(9.dp))

            Text(
                text = "A Dália está pensando... 💜",
                color = DaliaText,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun HistoryContent(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.94f))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Histórico de Chats",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DaliaText
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "🌸", fontSize = 48.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Suas conversas aparecerão aqui.",
            fontSize = 15.sp,
            color = DaliaText
        )

        Text(
            text = "Estamos preparando seu histórico!",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = DaliaPink
            )
        ) {
            Text("Voltar à conversa")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaliaBotScreenPreview() {
    Dalia2Theme {
        DaliaBotScreenContent(
            state = DaliaChatUiState(),
            onInputChanged = {},
            onSendMessage = {}
        )
    }
}
