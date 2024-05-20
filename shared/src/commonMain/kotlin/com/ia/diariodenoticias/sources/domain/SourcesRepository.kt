package com.ia.diariodenoticias.sources.domain

import com.ia.diariodenoticias.sources.data.model.SourceRaw

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
interface SourcesRepository {
    suspend fun getAllSources(): List<SourceRaw>
}