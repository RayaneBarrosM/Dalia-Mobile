package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme

@Composable
fun GeneralNewsScreen(
    newsList: List<ArticleItem> = emptyList(),
    onNewsClick: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("For You") }
    val categories = listOf("Para você", "Top", "Saúde", "legislação", "Bem-estar", "Trabalho")

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Carrossel de Categorias
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryChip(
                        categoryName = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Notícias
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(newsList) { item ->
                    NewsRowItem(
                        newsItem = item,
                        onClick = { onNewsClick(item.id) }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(top = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.Black.copy(alpha = 0.08f)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    categoryName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val chipBg = if (isSelected) Color(0xFFFFC0CB) else Color(0xFFEFEFEF)
    val textColor = if (isSelected) Color.Black else Color.Gray

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipBg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = categoryName,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor
        )
    }
}

@Composable
fun NewsRowItem(
    newsItem: ArticleItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        // Imagem da Notícia
        val imageRes = newsItem.imageResId ?: R.drawable.lotus
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Conteúdo do Texto
        Column(
            modifier = Modifier
                .weight(1f)
                .height(110.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = newsItem.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            if (newsItem.authorName.isNotBlank()) {
                Text(
                    text = "By ${newsItem.authorName}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = newsItem.categoryTag,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6) // Cor azul da categoria
                    )
                    Text(
                        text = "  •  ${newsItem.publishedTimeAgo}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "Opções",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneralNewsScreenPreview() {
    Dalia2Theme {
        val mockNewsList = listOf(
            ArticleItem(
                id = "1",
                title = "Monarch population soars 4,900 percent since last year in thrilling 2021 western migration",
                categoryTag = "World",
                authorName = "Andy Corbley",
                publishedTimeAgo = "1m ago",
                imageResId = R.drawable.lotus
            ),
            ArticleItem(
                id = "2",
                title = "The Horrifying Star Wars Holidays Special Has Been Given An Unofficial 4K Upgrade",
                categoryTag = "Entertainment",
                authorName = "Jane Smith",
                publishedTimeAgo = "5m ago",
                imageResId = R.drawable.lotus
            ),
            ArticleItem(
                id = "3",
                title = "NASA's $10 billion James Webb Space Telescope Launches An Epic Mission To Study Early Universe",
                categoryTag = "Science",
                authorName = "Maureen Jones",
                publishedTimeAgo = "20m ago",
                imageResId = R.drawable.lotus
            )
        )

        GeneralNewsScreen(newsList = mockNewsList)
    }
}