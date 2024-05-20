package com.ia.diariodenoticias.articles.data

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.ia.diariodenoticias.articles.data.impl.ArticlesDataSourceImpl
import com.ia.diariodenoticias.articles.data.impl.ArticlesRepositoryImpl
import com.ia.diariodenoticias.articles.data.model.ArticleRaw
import com.ia.diariodenoticias.articles.data.service.ArticlesService
import com.ia.diariodenoticias.articles.presentation.ArticlesState
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import com.ia.diariodenoticias.articles.usecase.Article
import com.ia.diariodenoticias.articles.usecase.GetArticlesUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.HttpClient
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
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
class ArticlesRepositoryTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<ArticlesRepository> { ArticlesRepositoryImpl(get()) }
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
    fun `Given an HTTP client When fetching top headlines Then the response should match the expected list of articles`() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.deployment.host" to "localhost")
        }
        val testHttpClient = createClient {
            install(HttpCookies)
            install(ContentNegotiation) {
                json()
            }
        }
        externalServices {
            hosts("localhost") {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
                routing {
                    get("/v2/top-headlines") {
                        call.respond(mountListOfArticles())
                    }
                }
            }
        }
        val articlesRepository = ArticlesRepositoryImpl(FakeArticlesDataSourceImpl(testHttpClient))

        assertEquals(mountListOfArticles(), articlesRepository.getArticles(true))
    }

    @Test
    fun `Given an HTTP client When fetching top headlines Then the response should not be an empty list`() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.deployment.host" to "localhost")
        }
        val testHttpClient = createClient {
            install(HttpCookies)
            install(ContentNegotiation) {
                json()
            }
        }
        externalServices {
            hosts("localhost") {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
                routing {
                    get("/v2/top-headlines") {
                        call.respond(mountListOfArticles())
                    }
                }
            }
        }
        val articlesRepository = ArticlesRepositoryImpl(FakeArticlesDataSourceImpl(testHttpClient))

        assertNotEquals(emptyList(), articlesRepository.getArticles(false))
    }

    private fun mountListOfArticles(): List<ArticleRaw> = listOf(
        ArticleRaw("title1", "desc1", "2024-05-18T22:27:49Z", "imageUrl1"),
        ArticleRaw("title2", "desc2", "2024-05-18T22:27:49Z", "imageUrl2"),
        ArticleRaw("title3", "desc3", "2024-05-18T22:27:49Z", "imageUrl3"),
        ArticleRaw("title4", "desc4", "2024-05-18T22:27:49Z", "imageUrl4"),
        ArticleRaw("title5", "desc5", "2024-05-18T22:27:49Z", "imageUrl5"),
    )

    inner class FakeArticlesDataSourceImpl(private val httpClient: HttpClient) : ArticlesDataSource {
        override fun getAllArticles(): List<ArticleRaw> {
            return emptyList()
        }

        override fun insertArticles(articles: List<ArticleRaw>) {
            return
        }

        override fun clearArticles() {
            return
        }

        override fun insertArticle(articleRaw: ArticleRaw) {
            return
        }

        override suspend fun fetchArticles(): List<ArticleRaw> {
            return httpClient.get("/v2/top-headlines") {
                header("hosts", "localhost")
            }.body()
        }

        override fun mapToArticleRaw(
            title: String,
            desc: String?,
            date: String,
            url: String?
        ): ArticleRaw =
            ArticleRaw(
                title,
                desc,
                date,
                url
            )
    }

}