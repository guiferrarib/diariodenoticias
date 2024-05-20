package com.ia.diariodenoticias.app.di

import com.ia.diariodenoticias.articles.presentation.di.articlesModule
import com.ia.diariodenoticias.sources.presentation.di.sourcesModule
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
val sharedKoinModules = listOf(
    articlesModule,
    sourcesModule
)