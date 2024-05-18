package com.ia.diariodenoticias.sources.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

@Serializable
data class SourceRaw(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val desc: String,
    @SerialName("language")
    val language: String,
    @SerialName("country")
    val country: String,
)