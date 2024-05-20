package com.ia.diariodenoticias.utils

import io.ktor.http.HttpStatusCode

/**
 * Copyright (c) 2024 AngelLira
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
data class MockResponse(
    val content: String,
    val status: HttpStatusCode
) {
    companion object {
        private fun ok(content: String) = MockResponse(content, HttpStatusCode.OK)
        fun default() = ok("{}")
    }

}