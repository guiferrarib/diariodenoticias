package com.ia.diariodenoticias.utils.di

import com.ia.diariodenoticias.app.di.networkModule
import com.ia.diariodenoticias.app.di.sharedKoinModules
import com.ia.diariodenoticias.app.di.startKoinApplication
import com.ia.diariodenoticias.articles.presentation.di.articlesModule
import com.ia.diariodenoticias.sources.presentation.di.sourcesModule
import org.koin.core.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.module.plus

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
fun startTestKoin(testModule: Module): KoinApplication {
    return startKoinApplication(
        listOf(testModule)
            .plus(sharedKoinModules)
            .plus(networkModule)
    )
}

fun stopTestKoin() {
    stopKoin()
}