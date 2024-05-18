package com.ia.diariodenoticias.android.di

import app.cash.sqldelight.db.SqlDriver
import com.ia.diariodenoticias.app.db.DatabaseDriverFactory
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

val databaseModule = module {

    single<SqlDriver> { DatabaseDriverFactory(androidContext()).createDriver() }

    single<DiarioDeNoticiasDatabase> { DiarioDeNoticiasDatabase(get()) }
}