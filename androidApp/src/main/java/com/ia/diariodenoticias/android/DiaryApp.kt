package com.ia.diariodenoticias.android

import android.app.Application
import com.ia.diariodenoticias.android.di.databaseModule
import com.ia.diariodenoticias.android.di.viewModelsModule
import com.ia.diariodenoticias.app.di.sharedKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class DiaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        val modules = sharedKoinModules + viewModelsModule + databaseModule

        startKoin {
            androidContext(this@DiaryApp)
            modules(modules)
        }
    }
}