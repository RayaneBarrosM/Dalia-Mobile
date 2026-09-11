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
import androidx.compose.runtime.mutableStateOf
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
import com.example.dalia2.ui.theme.viewmodel.QuizViewModel

@Composable
fun QuizPeriodScreen(
    viewModel: QuizViewModel,
    onQuizComplete: () -> Unit
) {
    // Lista das perguntas na ordem correta
    val perguntas = listOf(
        Pergunta(
            titulo = "Qual a sua faixa etária?",
            tipo = TipoPergunta.BOTAO,
            campo = "idade",
            opcoes = listOf(
                "Menor de 18" to 17,
                "18 a 30" to 25,
                "31 a 45 anos" to 35,
                "Mais de 45 anos" to 45
            )
        ),
        Pergunta(titulo ="Seu ciclo é regular?", tipo = TipoPergunta.BOTAO, campo = "cicloRegular",
        opcoes =listOf("Sim" to true, "Não" to false, "Não sei" to false)),
        Pergunta(titulo ="Você usa métodos contraceptivos?", tipo = TipoPergunta.BOTAO, campo = "contraceptivo",
        opcoes =listOf("Sim" to true, "Não" to false)),
        Pergunta(titulo ="Qual destes?", tipo = TipoPergunta.BOTAO, campo = "tipoContraceptivo",
        opcoes =listOf("Pilulas" to "Pilulas", "DIU" to "DIU", "Injeção" to "Injeção", "Implanou" to "Implanou", "Apenas preservativos" to "Apenas preservativos")),
        Pergunta(titulo ="Marque o dia da sua ultima menstruação", tipo = TipoPergunta.DATA, campo = "ultimaMenstruacao"),
        Pergunta(titulo ="Qual a duração média do seu ciclo?", tipo = TipoPergunta.NUMERO, campo = "duracaoCiclo")
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
                CampoNumero(dicaText = "Coloque um numero entre 21 a 35, se não souber coloque 28", label = "Duração", sufixo = "dias", intervalo = 21..35 , onValorConfirmado = { valor ->
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
                var dataInput by remember {mutableStateOf("")}
                CampoData(value = dataInput,onDataConfirmada = { data ->
                    dataInput = data
                    if(data.length == 10) {
                        viewModel.atualizarDadosQuiz(perguntaAtual.campo, data)
                        proximaPergunta(
                            lista = perguntas,
                            atual = indiceAtual,
                            valorSelecionado = data,
                            atualizarIndice = { indiceAtual = it },
                            finalizou = {
                                viewModel.onQuizFinish() // Chama o salvamento
                                onQuizComplete()         // Chama a navegação (agora sem erro!)
                            })
                    }
                }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            }
            TipoPergunta.BOTAO -> {
                BotoesOpcao(opcoes = perguntaAtual.opcoes, onSelecionado = { valor ->
                    viewModel.atualizarDadosQuiz(perguntaAtual.campo, valor)
                    proximaPergunta(lista = perguntas,
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
                    proximaPergunta(lista = perguntas,
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


fun proximaPergunta(lista: List<Pergunta>, atual: Int, valorSelecionado: Any, atualizarIndice: (Int) -> Unit, finalizou: () -> Unit) {

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizAScreenPreview() {
    Dalia2Theme {
        //QuizPeriodScreen{}
    }
}