package com.ia.diariodenoticias.di

import com.ia.diariodenoticias.app.di.networkModule
import com.ia.diariodenoticias.app.di.sharedKoinModules
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import com.ia.diariodenoticias.di.databaseModule
import com.ia.diariodenoticias.sources.presentation.SourcesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

fun initKoin() {

    val modules = sharedKoinModules + databaseModule + networkModule

    startKoin {
        modules(modules)
    }
}

class ArticlesInjector : KoinComponent {

    val articlesViewModel: ArticlesViewModel by inject()
}

class SourcesInjector : KoinComponent {

    val sourcesViewModel: SourcesViewModel by inject()
}