package com.ia.diariodenoticias.sources.data.model

import com.ia.diariodenoticias.sources.data.model.SourceRaw
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

@Serializable
data class SourcesResponse(
    @SerialName("status")
    val status: String,
    @SerialName("sources")
    val sources: List<SourceRaw>,
)