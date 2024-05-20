package com.ia.diariodenoticias.utils

import com.ia.diariodenoticias.sources.data.model.SourceRaw
import com.ia.diariodenoticias.sources.domain.Source

/**
 * Copyright (c) 2024 AngelLira
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
fun mapSources(sourcesRaw: List<SourceRaw>): List<Source> = sourcesRaw.map { raw ->
    Source(
        raw.id,
        raw.name,
        raw.desc,
        mapOrigin(raw),
    )
}

fun mapOrigin(raw: SourceRaw) = "${raw.country} - ${raw.language}"