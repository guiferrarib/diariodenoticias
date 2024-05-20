package com.ia.diariodenoticias.articles.domain

import com.ia.diariodenoticias.articles.data.model.ArticleRaw

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
interface ArticlesRepository {
    suspend fun getArticles(forceFetch: Boolean): List<ArticleRaw>

}