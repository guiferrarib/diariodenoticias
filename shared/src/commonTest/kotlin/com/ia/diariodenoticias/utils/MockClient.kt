package com.ia.diariodenoticias.utils

import io.ktor.client.request.HttpRequestData
import io.ktor.http.fullPath

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
class MockClient(
    private val defaultResponse: MockResponse = MockResponse.default(),
) : ResponseInterceptor {

    private var _call: (String) -> MockResponse = { defaultResponse }

    fun setResponse(response: MockResponse) {
        _call = { response }
    }

    override fun invoke(request: HttpRequestData): MockResponse {
        return _call.invoke(request.url.fullPath)
    }
}