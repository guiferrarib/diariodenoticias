package com.ia.diariodenoticias.android.di

import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import com.ia.diariodenoticias.sources.presentation.SourcesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

val viewModelsModule = module {

    viewModel { ArticlesViewModel(get()) }
    viewModel { SourcesViewModel(get()) }
}