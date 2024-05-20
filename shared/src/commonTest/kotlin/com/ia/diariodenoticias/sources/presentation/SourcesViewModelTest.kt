package com.ia.diariodenoticias.sources.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.ia.diariodenoticias.sources.data.SourcesRepository
import com.ia.diariodenoticias.sources.data.model.SourceRaw
import com.ia.diariodenoticias.sources.domain.GetSourcesUseCase
import com.ia.diariodenoticias.sources.domain.Source
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
class SourcesViewModelTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    private val viewModel: SourcesViewModel by inject()
    private val getSourcesUseCase: GetSourcesUseCase by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<SourcesRepository> { FakeSourcesRepositoryImpl() }
                    single { GetSourcesUseCase(get()) }
                    single { SourcesViewModel(get()) }
                }
            )
        }
        Dispatchers.setMain(dispatcherProvider)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain() // reset Main dispatcher to the original Main dispatcher
        dispatcherProvider.cancel()
    }

    @Test
    fun `Given successful API call When loadSources is called Then sources are loaded`() =
        runTest {
            getSourcesUseCase.getSources()
            turbineScope {
                viewModel.sourcesState.test {
                    assertEquals(SourcesState(emptyList(),loading = true), awaitItem())
                    assertEquals(SourcesState(loading = false, sources = mountListOfSourcesForCheck()), awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

    @Test
    fun `Given failed API call When loadSources is called Then empty list is returned`() = runTest {
        val getSourcesRepository = mock<SourcesRepository>()
        everySuspend { getSourcesRepository.getAllSources() } returns emptyList()
        val getArticlesUseCase = GetSourcesUseCase(getSourcesRepository)
        val sourcesViewModel = SourcesViewModel(getArticlesUseCase)
        turbineScope {
            sourcesViewModel.sourcesState.test {
                assertEquals(SourcesState(sources = emptyList() ,loading = true), awaitItem())
                assertEquals(SourcesState(sources = emptyList(),loading = false, error = null), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    private fun mountListOfSources(): List<SourceRaw> = listOf(
        SourceRaw("1","New York Times1", "desc1", "language1", "country1"),
        SourceRaw("2","New York Times2", "desc2", "language2", "country2"),
        SourceRaw("3","New York Times3", "desc3", "language3", "country3"),
        SourceRaw("4","New York Times4", "desc4", "language4", "country4"),
        SourceRaw("5","New York Times5", "desc5", "language5", "country5"),
    )

    private fun mountListOfSourcesForCheck() = listOf(
        Source("1", "New York Times1", "desc1", "country1 - language1"),
        Source("2", "New York Times2", "desc2", "country2 - language2"),
        Source("3", "New York Times3", "desc3", "country3 - language3"),
        Source("4", "New York Times4", "desc4", "country4 - language4"),
        Source("5", "New York Times5", "desc5", "country5 - language5"),
    )

    inner class FakeSourcesRepositoryImpl : SourcesRepository {
        override suspend fun getAllSources(): List<SourceRaw> {
            return mountListOfSources()
        }
    }

}