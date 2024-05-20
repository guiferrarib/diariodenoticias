package com.ia.diariodenoticias

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
internal expect class SharedFileReader() {
    fun loadJsonFile(fileName: String): String?
}