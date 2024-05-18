package com.ia.diariodenoticias.sources.presentation.di

import com.ia.diariodenoticias.sources.data.SourcesDataSource
import com.ia.diariodenoticias.sources.data.SourcesRepository
import org.koin.dsl.module
import com.ia.diariodenoticias.sources.data.impl.SourcesDataSourceImpl
import com.ia.diariodenoticias.sources.data.impl.SourcesRepositoryImpl
import com.ia.diariodenoticias.sources.data.service.SourcesService
import com.ia.diariodenoticias.sources.usecase.GetSourcesUseCase
import com.ia.diariodenoticias.sources.presentation.SourcesViewModel
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

val sourcesModule = module {

    single<SourcesService> { SourcesService(get()) }
    single<GetSourcesUseCase> { GetSourcesUseCase(get()) }
    single<SourcesDataSource> { SourcesDataSourceImpl(get()) }
    single<SourcesRepository> { SourcesRepositoryImpl(get(), get()) }
    single<SourcesViewModel> { SourcesViewModel(get()) }
}
