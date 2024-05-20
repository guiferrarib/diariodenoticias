package com.ia.diariodenoticias.sources.data.impl

import com.ia.diariodenoticias.sources.data.SourcesDataSource
import com.ia.diariodenoticias.sources.data.SourcesRepository
import com.ia.diariodenoticias.sources.data.model.SourceRaw
import com.ia.diariodenoticias.sources.data.service.SourcesService
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class SourcesRepositoryImpl(
    private val dataSource: SourcesDataSource
) : SourcesRepository {

    override suspend fun getAllSources(): List<SourceRaw> {
        val sourcesDb = dataSource.getAllSources()
        if (sourcesDb.isEmpty()) {
            dataSource.clearSources()
            val fetchedSources = dataSource.fetchSources()
            dataSource.createSources(fetchedSources)
            return fetchedSources
        }
        return sourcesDb
    }
}