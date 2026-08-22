package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Black
import com.example.dalia2.ui.theme.BlueButton
import com.example.dalia2.ui.theme.Purple
import com.example.dalia2.ui.theme.Red
import com.example.dalia2.ui.theme.Dalia2Theme
//import io.coil.compose.AsyncImage

// Data class
data class MeuItemPregnant(
    val id: Int,
    val tamanhoCm: Int,
    val peso: Int,
    val tamanho: String,
    val semana: Int
)

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val categoryTag: String,    // Ex: "Gestante", "Legislação", "Saúde"
    val imageUrl: String? = null, // Para imagens do banco
    val imageResId: Int? = null  // Fallback para imagens locais
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePregnantScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {}
) {
    val meusDados = listOf(
        MeuItemPregnant(1, 1, 2, "grão de mostarda", 1)
    )

    val noticiasDiarias = remember {
        listOf(
            NewsItem("1", "Cuidados na gestação", "Tudo sobre o primeiro trimestre.", "Gestante", imageResId = R.drawable.lotus),
            NewsItem("2", "Alimentação saudável", "Nutrientes essenciais para o bebê.", "Gestante", imageResId = R.drawable.lotus)
        )
    }

    val noticiasLegislacao = remember {
        listOf(
            NewsItem("3", "Direitos da gestante", "Conheça seus direitos no trabalho.", "Legislação", imageResId = R.drawable.lotus),
            NewsItem("4", "Atualizações no SUS", "Saiba o que mudou no atendimento.", "Legislação", imageResId = R.drawable.lotus)
        )
    }

    val scrollState = rememberScrollState()
    var selectedDay by remember { mutableStateOf(meusDados[0]) } // Corrigido para índice 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        // Seção de semanas de gravidez
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(Color(0xFFFF8A8A))
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = "Tamanho", fontSize = 14.sp, color = Color.DarkGray)
                        Text(text = "${selectedDay.tamanhoCm} cm", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lotus),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Peso", fontSize = 14.sp, color = Color.DarkGray)
                        Text(text = "${selectedDay.peso} g", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "O bebê está do tamanho de um ${selectedDay.tamanho}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Semana ${selectedDay.semana}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // Coluna de conteúdo principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hoje",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { /* Ação */ },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Purple)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Como você está se sentindo?",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { onNavigateToCalendar() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlueButton)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Carteira de vacinação e consultas",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dicas para esse momento tão importante",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                modifier = Modifier.align(Alignment.Start)
            )

            NewsCarouselPregnant(
                cardColor = Purple,
                newsItems = noticiasDiarias,
                onNewsClick = { newsId ->
                    // Redireciona para página do post
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Você sabe sobre as leis?",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            NewsCarouselPregnant(
                cardColor = Red,
                newsItems = noticiasLegislacao,
                onNewsClick = { newsId ->
                    // Redireciona para página do post
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun NewsCardPregnant(
    newsItem: NewsItem,
    cardColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                //Pega a Imagem pela URL
                if (!newsItem.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = newsItem.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (newsItem.imageResId != null) {
                    Image(
                        painter = painterResource(id = newsItem.imageResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.White
            ) {
                Text(
                    text = newsItem.categoryTag, //Vai fazer a diferenciação pela tag na mineração
                    fontSize = 10.sp,
                    color = cardColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = newsItem.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = newsItem.description,
                fontSize = 12.sp,
                color = Color.Black,
                maxLines = 2
            )
        }
    }
}

@Composable
fun NewsCarouselPregnant(
    cardColor: Color,
    newsItems: List<NewsItem>,
    onNewsClick: (String) -> Unit = {}
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(
            items = newsItems,
            key = { item -> item.id } // 'key' melhora a performance ao reordenar/renderizar pq pega o id e o compose não adiciona coisas
        ) { item ->
            NewsCardPregnant(
                newsItem = item,
                cardColor = cardColor,
                onClick = { onNewsClick(item.id)}
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun HomePregnantScreenPreview() {
    Dalia2Theme {
        HomePregnantScreen()
    }
}