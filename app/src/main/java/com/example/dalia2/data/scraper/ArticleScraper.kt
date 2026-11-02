package com.example.dalia2.data.scraper

import android.util.Log
import com.example.dalia2.ui.theme.screen.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object ArticleScraper {

    suspend fun extrairArtigo(url: String): Result<ArticleItem> = withContext(Dispatchers.IO) {
       Log.d("ARTICLE","dentro do extrair artigo")

        try {
            // Conecta à página simulando uma requisição de navegador
            val doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get()

            // 1. Título: pega a tag Open Graph ou a primeira tag <h1>
            val title = doc.select("meta[property=og:title]").attr("content")
                .ifBlank { doc.select("h1").first()?.text() ?: doc.title() }

            Log.d("ARTICLE","titulo pego: "+title)
            // 2. Descrição/Resumo
            val description = doc.select("meta[property=og:description]").attr("content")
                .ifBlank { doc.select("meta[name=description]").attr("content") }

            Log.d("ARTICLE","decrição pego: "+description)
            // 4. Autor
            val author = doc.select("meta[name=author]").attr("content")
                .ifBlank { doc.select("[rel=author], .author-name, .byline").first()?.text() ?: "Redação" }

            Log.d("ARTICLE","autor pego: "+author)
            // 5. Conteúdo: busca as tags de parágrafo <p> dentro do corpo principal
            val articleContainer = doc.select("article, .post-content, .entry-content, main")
            val paragraphs = if (articleContainer.isNotEmpty()) {
                articleContainer.select("p").eachText()
            } else {
                doc.select("p").eachText()
            }

            Log.d("ARTICLE","container pego: "+articleContainer)
            // Junta os parágrafos com quebras de linha duplas para leitura fluida
            val content = paragraphs.filter { it.length > 30 }.joinToString("\n\n")

            val item = ArticleItem(
                id = url.hashCode().toString(),
                title = title.trim(),
                description = description.trim(),
                content = content.trim(),
                categoryTag = "Gestação",
                authorName = author.trim(),
                publishedTimeAgo = "Artigo Online"
            )

            Log.d("ARTICLE", "Artigo extraído: $item")
            Result.success(item)
        } catch (e: Exception) {
            Log.d("ARTICLE", "falhou ao extrair :"+e.message+ " "+ e.toString() + " " + e )
            Result.failure(e)
        }
    }
}