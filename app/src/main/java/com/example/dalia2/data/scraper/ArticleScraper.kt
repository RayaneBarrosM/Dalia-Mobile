package com.example.dalia2.data.scraper

import android.util.Log
import com.example.dalia2.ui.theme.screen.ArticleItem
import com.example.dalia2.ui.theme.screen.ArticleSection
import com.example.dalia2.ui.theme.screen.SectionType
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
            val container = doc.select("article, .post-content, .entry-content, .article-body, main, #main-content").first() ?: doc.body()

            // 6. Seleciona h2, h3 e p na ordem em que aparecem na tela
            val elements = container.select("h2, h3, p")
            val sections = mutableListOf<ArticleSection>()

            for (el in elements) {
                val texto = el.text().trim()
                if (texto.isBlank()) continue

                when (el.tagName().lowercase()) {
                    "h2", "h3" -> {
                        // Subtítulo da matéria
                        sections.add(ArticleSection(type = SectionType.SUBTITLE, text = texto))
                    }
                    "p" -> {
                        // Filtra parágrafos minúsculos que costumam ser avisos de cookies ou botões
                        if (texto.length > 20) {
                            sections.add(ArticleSection(type = SectionType.PARAGRAPH, text = texto))
                        }
                    }
                }
            }

            val item = ArticleItem(
                id = url.hashCode().toString(),
                title = title.trim(),
                description = description.trim(),
                sections = sections,
                content = sections.joinToString("\n\n") { it.text },
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