package com.ia.diariodenoticias.app.db

import app.cash.sqldelight.db.SqlDriver
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DatabaseDriverFactory {

    fun createDriver(): SqlDriver
}