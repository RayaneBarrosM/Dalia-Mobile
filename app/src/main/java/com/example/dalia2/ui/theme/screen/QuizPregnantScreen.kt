package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.R
import com.example.dalia2.ui.components.BotoesMultiplaEscolha
import com.example.dalia2.ui.components.BotoesOpcao
import com.example.dalia2.ui.components.CampoData
import com.example.dalia2.ui.components.CampoNumero
import com.example.dalia2.ui.components.Pergunta
import com.example.dalia2.ui.components.TipoPergunta
import com.example.dalia2.ui.theme.Dalia2Theme
import com.example.dalia2.ui.theme.PinkButton
import com.example.dalia2.ui.theme.viewmodel.PregnancyQuizViewModel

@Composable
fun QuizPregnantScreen (

    viewModel: PregnancyQuizViewModel,
    onQuizComplete: () -> Unit
) {
    // Lista das perguntas na ordem correta
    val perguntas = listOf(
        Pergunta(
            titulo = "Confirme sua ultima menstruação", tipo = TipoPergunta.DATA, campo = "ultimaMenstruacao"
        ),
        Pergunta(titulo ="Quantas semenas de gestação você tem?", tipo = TipoPergunta.NUMERO, campo = "qtdSemanas"),
        Pergunta(titulo ="Qual a previsão de nascimento?", tipo = TipoPergunta.DATA, campo = "previsaoNascimento"),
        Pergunta(titulo ="Foi Planejada?", tipo = TipoPergunta.BOTAO, campo = "planejado",
            opcoes =listOf("Sim" to true, "Não" to false)),
        Pergunta(titulo ="Toma remedio controlado?", tipo = TipoPergunta.BOTAO, campo = "remedios",
            opcoes =listOf("Sim" to true, "Não" to false)),
        Pergunta(titulo ="Tem Algum desses habitos?", tipo = TipoPergunta.MULTIPLAESCOLHA, campo = "habitos",
            opcoes = listOf("Fumar" to "fumar", "Beber" to "beber", "Usar drogas ilicitas" to "drogas", "Nenhum" to false)),
        Pergunta(titulo ="Quais sintomas você tem?", tipo = TipoPergunta.MULTIPLAESCOLHA, campo = "sintomas",
            opcoes = listOf("Enjoos" to "enjoos", "Dor no seio" to "dorSeio", "Colicas leves" to "colicas", "Sangramento" to "sangramento", "Nenhum" to false))
    )

    var indiceAtual by remember { mutableIntStateOf(0) }
    val perguntaAtual = perguntas[indiceAtual]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Barra de Progresso
        LinearProgressIndicator(
            progress = (indiceAtual + 1).toFloat() / perguntas.size,
            modifier = Modifier.fillMaxWidth().clip(CircleShape),
            color = PinkButton
        )
        Spacer(modifier = Modifier.height(80.dp))

        // 1. Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(154.dp, 147.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = perguntaAtual.titulo,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (perguntaAtual.tipo) {
            TipoPergunta.NUMERO -> {
                CampoNumero(dicaText = "Deve ser de 1 a 40", label = "Semanas", sufixo = "semanas", intervalo = 1..42, onValorConfirmado = { valor ->
                    viewModel.atualizarDadosQuiz(perguntaAtual.campo, valor)
                    proximaPergunta(
                        lista = perguntas,
                        atual = indiceAtual,
                        valorSelecionado = valor,
                        atualizarIndice = { indiceAtual = it },
                        finalizou = {
                            viewModel.onQuizFinish() // Chama o salvamento
                            onQuizComplete()         // Chama a navegação (agora sem erro!)
                        })
                })
            }
            TipoPergunta.DATA -> {
                CampoData(onDataConfirmada = { data ->
                    viewModel.atualizarDadosQuiz(perguntaAtual.campo, data)
                    proximaPergunta(lista = perguntas,
                        atual = indiceAtual,
                        valorSelecionado = data,
                        atualizarIndice = { indiceAtual = it },
                        finalizou = {
                            viewModel.onQuizFinish() // Chama o salvamento
                            onQuizComplete()         // Chama a navegação (agora sem erro!)
                        })
                })
            }
            TipoPergunta.BOTAO -> {
                BotoesOpcao(opcoes = perguntaAtual.opcoes, onSelecionado = { valor ->
                    viewModel.atualizarDadosQuiz(perguntaAtual.campo, valor)
                    proximaPergunta2(lista = perguntas,
                        atual = indiceAtual,
                        valorSelecionado = valor,
                        atualizarIndice = { indiceAtual = it },
                        finalizou = {
                            viewModel.onQuizFinish() // Chama o salvamento
                            onQuizComplete()         // Chama a navegação (agora sem erro!)
                        })
                })
            }
            TipoPergunta.MULTIPLAESCOLHA -> {
                BotoesMultiplaEscolha(opcoes = perguntaAtual.opcoes, onConfirmado = { listaValores ->
                    viewModel.atualizarDadosQuiz(perguntaAtual.campo, listaValores)
                    proximaPergunta2(lista = perguntas,
                        atual = indiceAtual,
                        valorSelecionado = listaValores,
                        atualizarIndice = { indiceAtual = it },
                        finalizou = {
                            viewModel.onQuizFinish() // Chama o salvamento
                            onQuizComplete()         // Chama a navegação (agora sem erro!)
                        })
                })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

    }
}


fun proximaPergunta2(lista: List<Pergunta>, atual: Int, valorSelecionado: Any, atualizarIndice: (Int) -> Unit, finalizou: () -> Unit) {

    val perguntaAtual = lista[atual]
    if(perguntaAtual.campo == "contraceptivo" && valorSelecionado == false){
        if (atual + 2 < lista.size) {
            atualizarIndice(atual + 2)
        } else {
            finalizou()
        }
    } else {
        if (atual < lista.size - 1) {
            atualizarIndice(atual + 1)
        } else {
            finalizou()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun QuizPregnant1ScreenPreview() {
    Dalia2Theme {

    }
}