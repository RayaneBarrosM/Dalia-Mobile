package com.example.dalia2.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dalia2.data.model.Comments
import com.example.dalia2.data.model.Posts
import com.example.dalia2.data.repository.DaliaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ForumViewModel  @Inject constructor(
    private val repository: DaliaRepository
): ViewModel(){
    var postSucess by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf<Boolean?>(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set


    private val _posts = MutableStateFlow<List<Posts>>(emptyList())
    val posts = _posts.asStateFlow()

    fun carregarPosts() {
        viewModelScope.launch {
        isLoading = true
        repository.getPosts()
            .onSuccess { lista ->
                _posts.value = lista.sortedByDescending { it.createdAt }
            }
            .onFailure {
                errorMessage = it.message
            }
        isLoading = false
    }}

    fun criarNovoPost(titulo: String, categoria: String, conteudo: String, onSucess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true

            val timestamp = Instant.now().toString()

            val novoPost = Posts(
                idPost = "", // API gera
                idUser = "id_USER",
                title = titulo,
                category = categoria,
                content = conteudo,
                likesValue = 0,
                createdAt = timestamp,
                comments = emptyList()
            )
            repository.createPost(novoPost).onSuccess { it
                carregarPosts()
                onSucess()
            }.onFailure {
                Log.d("API_ERROR","falaha ao criar: ${it.message}")
            }
             isLoading = true
        }
    }

    fun addComentario(postId: String, comentario: String) {
        viewModelScope.launch{

            val timestamp = Instant.now().toString()

            val novoComentario = Comments(
                idComment = "",
                idUser = "id_USER",
                comment = comentario,
                createdAt = timestamp
            )

            val response =repository.createComment(postId, novoComentario)
            if (response.isSuccess) {
                Log.d("API_ERROR","falaha ao criar: ${response.getOrNull()}")
                carregarPosts()
            }
            isLoading = true
        }
    }

    fun toggleLike(postId: String?, isAlreadyLiked: Boolean) {
        if (postId == null) return

        viewModelScope.launch {
            if (isAlreadyLiked) {
                repository.unlikePost(postId)
            } else {
                repository.likePost(postId)
            }
            carregarPosts()
        }
    }
}