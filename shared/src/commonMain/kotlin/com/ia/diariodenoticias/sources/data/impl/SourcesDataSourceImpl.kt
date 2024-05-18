package com.ia.diariodenoticias.sources.data.impl

import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
import com.ia.diariodenoticias.sources.data.SourcesDataSource
import com.ia.diariodenoticias.sources.data.model.SourceRaw
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class SourcesDataSourceImpl(private val db: DiarioDeNoticiasDatabase) : SourcesDataSource {

    override fun getAllSources(): List<SourceRaw> =
        db.diarioDeNoticiasDatabaseQueries.selectAllSources(::mapSource).executeAsList()

    override fun clearSources() =
        db.diarioDeNoticiasDatabaseQueries.removeAllSources()

    override fun mapSource(
        id: String,
        name: String,
        desc: String,
        language: String,
        country: String
    ): SourceRaw {
        return SourceRaw(
            id,
            name,
            desc,
            language,
            country
        )
    }

    override fun createSources(sources: List<SourceRaw>) {
        db.diarioDeNoticiasDatabaseQueries.transaction {
            sources.forEach { source ->
                insertSource(source)
            }
        }
    }

    override fun insertSource(source: SourceRaw) {
        db.diarioDeNoticiasDatabaseQueries.insertSource(
            source.id,
            source.name,
            source.desc,
            source.language,
            source.country,
        )
    }
}
