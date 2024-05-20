package com.ia.diariodenoticias.app.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.plus
import org.koin.dsl.module

/**
 * Copyright (c) 2024 AngelLira
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
fun startKoinApplication(appModule: Module = module { }): KoinApplication {
    return startKoinApplication(
        listOf(appModule, networkModule).plus(sharedKoinModules)
    )
}

fun startKoinApplication(modules: List<Module>): KoinApplication {
    return startKoin {
        modules(modules)
    }
}