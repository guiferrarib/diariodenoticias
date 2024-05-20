package com.ia.diariodenoticias

/**
 * Copyright (c) 2024 AngelLira
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
import android.content.Context
internal actual class SharedFileReader{


    lateinit var appContext: Context
    actual fun loadJsonFile(fileName: String): String? {
        val inputStream = Context::class.java.classLoader!!.getResourceAsStream(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }
}