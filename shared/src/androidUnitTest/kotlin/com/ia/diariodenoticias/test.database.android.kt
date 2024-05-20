package com.ia.diariodenoticias

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase

/**
 * Copyright (c) 2024 AngelLira
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
actual fun testDbDriver(): SqlDriver {
    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also {
            DiarioDeNoticiasDatabase.Schema.create(it)
        }
}