package com.ia.diariodenoticias

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
actual fun testDbDriver(): SqlDriver {
    return inMemoryDriver(DiarioDeNoticiasDatabase.Schema)
}