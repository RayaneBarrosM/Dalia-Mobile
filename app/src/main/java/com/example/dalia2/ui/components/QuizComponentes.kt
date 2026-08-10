package com.example.dalia2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.ui.theme.GrayButton
import com.example.dalia2.ui.theme.PinkButton
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

enum class TipoPergunta { BOTAO, DATA, NUMERO, MULTIPLAESCOLHA }

data class Pergunta(
    val titulo: String,
    val tipo: TipoPergunta,
    val campo: String,
    val opcoes: List<Pair<String, Any>> = emptyList()
)

@Composable
fun BotoesOpcao(
    opcoes: List<Pair<String, Any>>,
    onSelecionado: (Any) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espaçamento entre os botões
    ) {
        opcoes.forEach { (texto, valor) ->
            Button(
                onClick = {
                    onSelecionado(valor)
                },
                modifier = Modifier.size(width = 304.dp, height = 44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GrayButton),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = texto, fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoData(
    onDataConfirmada: (String) -> Unit
) {
    var showDatePicker by remember { androidx.compose.runtime.mutableStateOf(false) }
    val datePickerState = androidx.compose.material3.rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Espaçamento entre os botões
    ) {
        OutlinedButton(onClick = { showDatePicker = true }) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(text = datePickerState.selectedDateMillis?.let {
                formatarData(it)
            } ?: "Selecionar data")
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedDate = formatarData(datePickerState.selectedDateMillis)
                        showDatePicker = false
                        onDataConfirmada(selectedDate)
                    }) { Text("Confirmar") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun CampoNumero(
    dicaText: String = "dica aaqui",
    label: String = "duranção/ semanas",
    sufixo: String = "dias/ semanas",
    intervalo: IntRange = 1..100,
    onValorConfirmado: (Int) -> Unit
) {
    var textoDigitado by remember { androidx.compose.runtime.mutableStateOf("") }
    var erroValidacao by remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espaçamento entre os botões
    ) {
        Text(
            text = dicaText,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = textoDigitado,
            onValueChange = { novoTexto ->
                if (novoTexto.all { it.isDigit() } && novoTexto.length <= 2) {
                    textoDigitado = novoTexto
                    val valor = novoTexto.toIntOrNull()
                    erroValidacao = valor != null && valor !in intervalo
                }
            },
            label = { Text(label) },
            suffix = { Text(sufixo) }, // Fica bonitinho mostrar a unidade dentro do campo
            isError = erroValidacao, // Ativa a cor vermelha do Material Design
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val valorFinal = textoDigitado.toIntOrNull() ?: 28
                onValorConfirmado(valorFinal) },
            // O botão só habilita se o valor for válido
            enabled = !erroValidacao && textoDigitado.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PinkButton
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Próximo", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun BotoesMultiplaEscolha(
    opcoes: List<Pair<String, Any>>,
    onConfirmado: (List<Any>) -> Unit
) {
    // Guarda a lista de valores selecionados
    val selecionados = remember { mutableStateListOf<Any>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        opcoes.forEach { (texto, valor) ->
            val estaSelecionado = selecionados.contains(valor)

            Button(
                onClick = {
                    if (estaSelecionado) {
                        selecionados.remove(valor)
                    } else {
                        selecionados.add(valor)
                    }
                },
                modifier = Modifier.size(width = 304.dp, height = 44.dp),
                colors = ButtonDefaults.buttonColors(
                    // Altera a cor se o botão estiver marcado
                    containerColor = if (estaSelecionado) PinkButton else GrayButton,
                    contentColor = if (estaSelecionado) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = texto, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botão de envio da lista final
        Button(
            onClick = { onConfirmado(selecionados.toList()) },
            enabled = selecionados.isNotEmpty(), // Só habilita se marcar ao menos um
            colors = ButtonDefaults.buttonColors(containerColor = PinkButton),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Confirmar Seleção", color = Color.White, fontSize = 16.sp)
        }
    }
}

fun formatarData(millis: Long?): String {
    if (millis == null) return ""
    val data = Instant.ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        // Formato padrão para APIs: AAAA-MM-DD
    return data.format(DateTimeFormatter.ISO_LOCAL_DATE)
}