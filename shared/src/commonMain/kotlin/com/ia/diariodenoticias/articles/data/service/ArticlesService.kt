package com.ia.diariodenoticias.articles.data.service

import com.ia.diariodenoticias.app.utils.getApiKey
import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import com.ia.diariodenoticias.articles.data.model.ArticlesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.utils.EmptyContent.headers

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
open class ArticlesService(private val httpClient: HttpClient) {

    private val country = "us"
    private val category = "business"

    suspend fun fetchArticles(): List<ArticleRaw> {
        val response: ArticlesResponse = httpClient
            .get("https://newsapi.org/v2/top-headlines?country=$country&category=$category&apiKey=${getApiKey()}") {
                headers {
                    append("Content-Type", "application/json")
                }
            }
            .body()
        return response.articles
    }
}
