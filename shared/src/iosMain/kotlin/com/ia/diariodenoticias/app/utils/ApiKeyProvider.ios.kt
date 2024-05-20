package com.ia.diariodenoticias.app.utils

import platform.Foundation.NSBundle

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
actual fun getApiKey(): String {
    return NSBundle.mainBundle.objectForInfoDictionaryKey("API_KEY") as? String ?: "default_value"
}