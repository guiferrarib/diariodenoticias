package com.ia.diariodenoticias.app

import kotlinx.coroutines.CoroutineScope
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect open class BaseViewModel() {

    val scope: CoroutineScope
}