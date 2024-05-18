package com.ia.diariodenoticias.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class BaseViewModel: ViewModel() {

    actual val scope = viewModelScope
}