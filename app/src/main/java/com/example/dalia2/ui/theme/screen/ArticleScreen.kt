package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.R
import com.example.dalia2.ui.theme.Dalia2Theme

data class ArticleItem(
    val id: String,
    val title: String,
    val description: String = "",
    val content: String = "",
    val categoryTag: String,
    val imageUrl: String? = null,
    val imageResId: Int? = null,
    val authorName: String = "",
    val authorImageUrl: String? = null,
    val authorImageResId: Int? = null,
    val publishedTimeAgo: String = "",
    val isBookmarked: Boolean = false
)
@Composable
fun ArticleScreen(
    articleItem: ArticleItem,
    onBackClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onBackClick() }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Salvar",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onBookmarkClick() }
                    )
                    // Botão de compartilhar modificado
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartilhar",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onShareClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagem Principal
            Image(
                painter = painterResource(id = R.drawable.lotus), // Substitua pela sua imagem do artigo
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Título do Artigo
            Text(
                text = articleItem.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Perfil do Autor e Tempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Essa parte deve ser auterada para conseguir usar a imagem correta
                Image(
                    painter = painterResource(R.drawable.lotus),
                    contentDescription = "Foto do Autor",
                    modifier = Modifier
                        .size(48.dp) // Corrigido tamanho da foto de perfil
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = articleItem.authorName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = articleItem.publishedTimeAgo,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Corpo do Texto
            Text(
                text = articleItem.content,
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.85f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    Dalia2Theme {
        // Exemplo de como a sua lista ou ViewModel alimentará o item
        val artigoExemplo = ArticleItem(
            id = "1",
            title = "Cuidados na gestação",
            description = "Tudo sobre o primeiro trimestre.", // Vai para o Card
            content = """
        Integer tristique, felis id eleifend ultrices, tellus libero venenatis tellus, non pulvinar nibh ipsum at ante. 
        
        Aenean pellentesque leo interdum ligula egestas viverra. Maecenas consectetur, massa nec lobortis placerat, nulla diam ullamcorper ex.
    """.trimIndent(), // Vai para a tela inteira do Artigo
            categoryTag = "Gestante",
            imageResId = R.drawable.lotus
        )
        ArticleScreen(articleItem = artigoExemplo)
    }
}