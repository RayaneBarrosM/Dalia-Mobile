package com.example.dalia2.data.model

import com.google.gson.annotations.SerializedName

data class Articles(
    @SerializedName("id")
    val idArticle: String,
    val title: String,
    //val legend: String,
    val link: String,
    val category: String
)