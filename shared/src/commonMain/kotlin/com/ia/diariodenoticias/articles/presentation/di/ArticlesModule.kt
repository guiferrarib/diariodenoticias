package com.ia.diariodenoticias.articles.presentation.di

import com.ia.diariodenoticias.articles.data.ArticlesDataSource
import com.ia.diariodenoticias.articles.domain.ArticlesRepository
import com.ia.diariodenoticias.articles.data.impl.ArticlesDataSourceImpl
import com.ia.diariodenoticias.articles.data.impl.ArticlesRepositoryImpl
import com.ia.diariodenoticias.articles.data.service.ArticlesService
import com.ia.diariodenoticias.articles.domain.GetArticlesUseCase
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import org.koin.dsl.module
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

val articlesModule = module {
    single<ArticlesService> { ArticlesService(get()) }
    single<ArticlesDataSource> { ArticlesDataSourceImpl(get(),get()) }
    single<ArticlesRepository> { ArticlesRepositoryImpl(get()) }
    single { GetArticlesUseCase(get()) }
    single { ArticlesViewModel(get()) }
}