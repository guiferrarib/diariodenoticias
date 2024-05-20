package com.ia.diariodenoticias.articles.data

import com.ia.diariodenoticias.articles.data.model.ArticleRaw

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

interface ArticlesDataSource {
    fun getAllArticles(): List<ArticleRaw>
    fun insertArticles(articles: List<ArticleRaw>)

    fun clearArticles()

    fun insertArticle(articleRaw: ArticleRaw)

    suspend fun fetchArticles(): List<ArticleRaw>
    fun mapToArticleRaw(title: String, desc: String?, date: String, url: String?): ArticleRaw
}