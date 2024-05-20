package com.ia.diariodenoticias.utils.di

import app.cash.sqldelight.db.SqlDriver
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
import com.ia.diariodenoticias.testDbDriver
import com.ia.diariodenoticias.utils.MockClient
import com.ia.diariodenoticias.utils.testKtorClient
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
val testPlatformModule: Module = module {
    single<DiarioDeNoticiasDatabase> { DiarioDeNoticiasDatabase(testDbDriver()) }
    single<SqlDriver> { testDbDriver() }
    single<MockClient> { MockClient() }
    single<HttpClient> { testKtorClient(get()) }
}