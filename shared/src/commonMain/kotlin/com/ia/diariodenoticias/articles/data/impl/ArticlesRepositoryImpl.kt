package com.ia.diariodenoticias.articles.data.impl

import com.ia.diariodenoticias.articles.data.ArticlesDataSource
import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import com.ia.diariodenoticias.articles.data.ArticlesRepository
import com.ia.diariodenoticias.articles.data.service.ArticlesService
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class ArticlesRepositoryImpl(
    private val dataSource: ArticlesDataSource
) : ArticlesRepository {

    override suspend fun getArticles(forceFetch: Boolean): List<ArticleRaw> {
        if (forceFetch) {
            dataSource.clearArticles()
            return dataSource.fetchArticles()
        }

        val articlesDb = dataSource.getAllArticles()
        println("Got ${articlesDb.size} from the database!!")

        if (articlesDb.isEmpty()) {
            return dataSource.fetchArticles()
        }

        return articlesDb
    }
}