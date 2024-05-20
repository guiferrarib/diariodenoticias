package com.ia.diariodenoticias.articles.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.ia.diariodenoticias.articles.data.ArticlesRepository
import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import com.ia.diariodenoticias.articles.presentation.ArticlesState
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import com.ia.diariodenoticias.articles.usecase.Article
import com.ia.diariodenoticias.articles.usecase.GetArticlesUseCase
import com.ia.diariodenoticias.utils.mapArticles
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.testApplication
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
class ArticlesViewModelTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    private val viewModel: ArticlesViewModel by inject()
    private val getArticlesUseCase: GetArticlesUseCase by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<ArticlesRepository> { FakeArticlesRepositoryImpl() }
                    single { GetArticlesUseCase(get()) }
                    single { ArticlesViewModel(get()) }
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
    fun `Given successful API call When loadArticles is called Then articles are loaded`() =
        runTest {
            turbineScope {
                viewModel.getArticles()
                viewModel.articlesState.test {
                    assertEquals(ArticlesState(loading = true), awaitItem())
                    assertEquals(ArticlesState(loading = false, articles = mapArticles(mountListOfArticles())), awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

    @Test
    fun `Given failed API call When loadArticles is called Then empty list is returned`() = runTest {
        val getArticlesRepository = mock<ArticlesRepository>()
        everySuspend { getArticlesRepository.getArticles(any()) } returns emptyList()
        val getArticlesUseCase = GetArticlesUseCase(getArticlesRepository)
        val articlesViewModel = ArticlesViewModel(getArticlesUseCase)
     turbineScope {
         articlesViewModel.articlesState.test {
                assertEquals(ArticlesState(loading = true), awaitItem())
                assertEquals(ArticlesState(articles = emptyList(),loading = false, error = null), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
     }
    }

    private fun mountListOfArticles(): List<ArticleRaw> = listOf(
        ArticleRaw("title1", "desc1", "2024-05-18T22:27:49Z", "imageUrl1"),
        ArticleRaw("title2", "desc2", "2024-05-18T22:27:49Z", "imageUrl2"),
        ArticleRaw("title3", "desc3", "2024-05-18T22:27:49Z", "imageUrl3"),
        ArticleRaw("title4", "desc4", "2024-05-18T22:27:49Z", "imageUrl4"),
        ArticleRaw("title5", "desc5", "2024-05-18T22:27:49Z", "imageUrl5"),
    )

    inner class FakeArticlesRepositoryImpl : ArticlesRepository {

        override suspend fun getArticles(forceFetch: Boolean): List<ArticleRaw> {
            return mountListOfArticles()
        }
    }

}