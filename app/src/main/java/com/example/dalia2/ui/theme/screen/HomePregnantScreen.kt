package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
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
import androidx.compose.ui.graphics.Brush
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
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToArticle: (String) -> Unit = {},
    onNavigateToGeneralNews: () -> Unit = {}
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
                .background(brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFA7E8F), // Cor do Centro
                        Color(0xFFFF8E8E)  // Cor das Bordas
                    )))
                .padding(vertical = 30.dp, horizontal = 16.dp)
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
            //Cards Fixos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { onNavigateToRegister() },
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
                        Image(
                            painter = painterResource(id = R.drawable.hugheart),
                            contentDescription = null
                        )
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
                        Image(
                            painter = painterResource(id = R.drawable.heartcalendar),
                            contentDescription = null
                        )
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

            Spacer(modifier = Modifier.height(24.dp))

            NewsCarouselPregnant(
                cardColor = Purple,
                newsItems = noticiasDiarias,
                onNewsClick = { newsId ->
                    onNavigateToArticle(newsId)// Redireciona para página do post
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom // Alinha o "Saiba mais" com a base do subtítulo
            ) {
                // Título e Subtítulo
                Column {
                    Text(
                        text = "Você sabe sobre as leis?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black
                    )
                    Text(
                        text = "Saiba como se defender",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }

                // Botão: "Saiba mais"
                Row(
                    modifier = Modifier.clickable {
                        onNavigateToGeneralNews()
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Saiba mais",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6) // Azul do botão
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            NewsCarouselPregnant(
                cardColor = Red,
                newsItems = noticiasLegislacao,
                onNewsClick = { newsId ->
                    onNavigateToArticle(newsId)
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
            .width(240.dp)
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
                /*Pega a Imagem pela URL
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
                }*/

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Solução temporária: usa imagem local enquanto a URL/Coil não carrega
                    val imageRes = newsItem.imageResId ?: R.drawable.lotus // Ajuste para uma imagem do seu drawable

                    Image(
                        painter = painterResource(id = imageRes),
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