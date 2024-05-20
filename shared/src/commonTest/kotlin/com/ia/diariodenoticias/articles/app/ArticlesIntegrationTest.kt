package com.ia.diariodenoticias.articles.app

import app.cash.turbine.test
import com.ia.diariodenoticias.app.di.sharedKoinModules
import com.ia.diariodenoticias.articles.data.ArticlesDataSource
import com.ia.diariodenoticias.articles.data.ArticlesRepository
import com.ia.diariodenoticias.articles.data.impl.ArticlesDataSourceImpl
import com.ia.diariodenoticias.articles.data.impl.ArticlesRepositoryImpl
import com.ia.diariodenoticias.articles.data.model.ArticlesResponse
import com.ia.diariodenoticias.articles.data.service.ArticlesService
import com.ia.diariodenoticias.articles.presentation.ArticlesState
import com.ia.diariodenoticias.articles.presentation.ArticlesViewModel
import com.ia.diariodenoticias.articles.usecase.Article
import com.ia.diariodenoticias.articles.usecase.GetArticlesUseCase
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
import com.ia.diariodenoticias.testDbDriver
import com.ia.diariodenoticias.utils.JsonLoader
import com.ia.diariodenoticias.utils.MockClient
import com.ia.diariodenoticias.utils.MockResponse
import com.ia.diariodenoticias.utils.di.stopTestKoin
import com.ia.diariodenoticias.utils.di.testPlatformModule
import com.ia.diariodenoticias.utils.mapArticles
import com.ia.diariodenoticias.utils.testKtorClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ArticlesIntegrationTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    private val jsonResponse = JsonLoader().load("articles.json")

    private val viewModel: ArticlesViewModel by inject()

    private val localDataSource: ArticlesDataSource by inject()

    private val mockClient: MockClient by inject()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                testPlatformModule + sharedKoinModules
            )
        }
        Dispatchers.setMain(dispatcherProvider)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        stopTestKoin()
        Dispatchers.resetMain() // reset Main dispatcher to the original Main dispatcher
        dispatcherProvider.cancel()
    }
    @Test
    fun `test fetching articles updates viewmodel state and stores data locally`() = runTest {
        //Given
        val response = MockResponse(jsonResponse, HttpStatusCode.OK)
        mockClient.setResponse(response)
        //When
        viewModel.getArticles(true)
        //Then
        viewModel.articlesState.test {
            // Loading State
            assertEquals(ArticlesState(loading = true), awaitItem())
            // Items loaded by viewmodel
            assertEquals(ArticlesState(loading = false, articles = mapJsonToArticlesState(jsonResponse)), awaitItem())
            // Items stored in local database
            assertEquals(true, localDataSource.getAllArticles().isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun mapJsonToArticlesState(json: String): List<Article> {
        val jsonSerializer = Json {
            explicitNulls = false
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
        val articles = jsonSerializer.decodeFromString<ArticlesResponse>(json).articles
        return mapArticles(articles)
    }

}