package com.ia.diariodenoticias.sources.presentation

import com.ia.diariodenoticias.sources.usecase.Source
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

data class SourcesState (
    val sources: List<Source>,
    val loading: Boolean = false,
    val error: String? = null
)
