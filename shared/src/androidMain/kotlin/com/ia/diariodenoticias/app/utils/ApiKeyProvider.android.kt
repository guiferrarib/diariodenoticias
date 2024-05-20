package com.ia.diariodenoticias.app.utils

import com.ia.diariodenoticias.BuildConfig

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
actual fun getApiKey(): String {
    return BuildConfig.API_KEY
}