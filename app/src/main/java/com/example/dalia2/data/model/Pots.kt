package com.example.dalia2.data.model

import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("id")
    val idPost: String,
    val idUser: String,
    val title: String,
    val category: String,
    val content: String,
    @SerializedName("likes")
    val likesValue: Int,
    val createdAt: String,
    val comments: List<Comments>
)

data class Comments(
    val idComment:String,
    val idUser: String,
    val comment:String,
    val createdAt: String
)
