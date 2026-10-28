package com.example.dalia2.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.scraper.ArticleScraper
import com.example.dalia2.ui.theme.screen.ArticleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor() : ViewModel() {

    var artigoState by mutableStateOf<ArticleItem?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun carregarArtigoDeUrl(url: String) {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val resultado = ArticleScraper.extrairArtigo(url)
            if (resultado.isSuccess) {
                artigoState = resultado.getOrNull()
            } else {
                errorMessage = "Não foi possível carregar o artigo do site."
            }
            isLoading = false
        }
    }
}