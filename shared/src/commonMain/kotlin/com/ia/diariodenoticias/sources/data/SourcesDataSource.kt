package com.ia.diariodenoticias.sources.data

import com.ia.diariodenoticias.sources.data.model.SourceRaw

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
interface SourcesDataSource {

    fun getAllSources(): List<SourceRaw>

    fun clearSources()

    fun createSources(sources: List<SourceRaw>)
    fun insertSource(source: SourceRaw)

    suspend fun fetchSources(): List<SourceRaw>

    fun mapSource(
        id: String,
        name: String,
        desc: String,
        language: String,
        country: String
    ): SourceRaw
}