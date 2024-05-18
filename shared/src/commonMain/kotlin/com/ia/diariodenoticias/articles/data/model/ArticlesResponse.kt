package com.ia.diariodenoticias.articles.data.model

import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
@Serializable
data class ArticlesResponse(
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val results: Int,
    @SerialName("articles")
    val articles: List<ArticleRaw>
)