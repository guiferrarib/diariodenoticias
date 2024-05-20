package com.ia.diariodenoticias.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
 fun testKtorClient(mockClient: MockClient = MockClient()): HttpClient {
    val engine = testKtorEngine(mockClient)
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
}

 fun testKtorEngine(interceptor: ResponseInterceptor) = MockEngine { request ->
    val response = interceptor(request)
    respond(
        content = ByteReadChannel(response.content),
        status = response.status,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}