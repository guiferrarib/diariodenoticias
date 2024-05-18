package com.ia.diariodenoticias.articles.data.impl

import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import com.ia.diariodenoticias.articles.data.ArticlesDataSource
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class ArticlesDataSourceImpl(private val database: DiarioDeNoticiasDatabase) : ArticlesDataSource {

    override fun getAllArticles(): List<ArticleRaw> =
        database.diarioDeNoticiasDatabaseQueries.selectAllArticles(::mapToArticleRaw).executeAsList()

    override fun insertArticles(articles: List<ArticleRaw>) {
        database.diarioDeNoticiasDatabaseQueries.transaction {
            articles.forEach { articleRaw ->
                insertArticle(articleRaw)
            }
        }
    }

    override fun clearArticles() =
        database.diarioDeNoticiasDatabaseQueries.removeAllArticles()

    override fun insertArticle(articleRaw: ArticleRaw) {
        database.diarioDeNoticiasDatabaseQueries.insertArticle(
            articleRaw.title,
            articleRaw.desc,
            articleRaw.date,
            articleRaw.imageUrl
        )
    }

    private fun mapToArticleRaw(
        title: String,
        desc: String?,
        date: String,
        url: String?
    ): ArticleRaw =
        ArticleRaw(
            title,
            desc,
            date,
            url
        )
}