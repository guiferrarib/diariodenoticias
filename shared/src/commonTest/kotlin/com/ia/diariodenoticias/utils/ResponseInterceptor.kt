package com.ia.diariodenoticias.utils

import io.ktor.client.request.HttpRequestData

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
fun interface ResponseInterceptor {
    operator fun invoke(request: HttpRequestData): MockResponse
}