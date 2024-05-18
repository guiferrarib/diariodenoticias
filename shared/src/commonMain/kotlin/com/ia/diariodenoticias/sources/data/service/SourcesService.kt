package com.ia.diariodenoticias.sources.data.service

import com.ia.diariodenoticias.sources.data.model.SourceRaw
import com.ia.diariodenoticias.sources.data.model.SourcesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class SourcesService(private val httpClient: HttpClient) {
    private val apiKey = "23c183a5a43c4637ab7be43476b0f10e"
    suspend fun fetchSources(): List<SourceRaw> {

        val response: SourcesResponse =
            httpClient.get("https://newsapi.org/v2/top-headlines/sources?apiKey=$apiKey").body()
        return response.sources
    }
}
