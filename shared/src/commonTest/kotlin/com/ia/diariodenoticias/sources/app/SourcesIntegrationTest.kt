package com.ia.diariodenoticias.sources.app

import app.cash.turbine.test
import com.ia.diariodenoticias.db.DiarioDeNoticiasDatabase
import com.ia.diariodenoticias.sources.data.SourcesDataSource
import com.ia.diariodenoticias.sources.data.SourcesRepository
import com.ia.diariodenoticias.sources.data.impl.SourcesDataSourceImpl
import com.ia.diariodenoticias.sources.data.impl.SourcesRepositoryImpl
import com.ia.diariodenoticias.sources.data.model.SourcesResponse
import com.ia.diariodenoticias.sources.data.service.SourcesService
import com.ia.diariodenoticias.sources.presentation.SourcesState
import com.ia.diariodenoticias.sources.presentation.SourcesViewModel
import com.ia.diariodenoticias.sources.usecase.GetSourcesUseCase
import com.ia.diariodenoticias.sources.usecase.Source
import com.ia.diariodenoticias.testDbDriver
import com.ia.diariodenoticias.utils.JsonLoader
import com.ia.diariodenoticias.utils.MockClient
import com.ia.diariodenoticias.utils.MockResponse
import com.ia.diariodenoticias.utils.mapSources
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

class SourcesIntegrationTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    private val jsonResponse = JsonLoader().load("sources.json")

    private val viewModel: SourcesViewModel by inject()

    private val localDataSource: SourcesDataSource by inject()

    private val mockClient = MockClient()


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<DiarioDeNoticiasDatabase> { DiarioDeNoticiasDatabase(testDbDriver()) }
                    single<SourcesService> { SourcesService(testKtorClient(mockClient)) }
                    single<SourcesDataSource> { SourcesDataSourceImpl(get(), get()) }
                    single<SourcesRepository> { SourcesRepositoryImpl(get()) }
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
    fun `test fetching sources updates viewmodel state and stores data locally`() = runTest {
        //Given
        val response = MockResponse(jsonResponse, HttpStatusCode.OK)
        mockClient.setResponse(response)
        //When
        viewModel.getSources()
        //Then
        viewModel.sourcesState.test {
            // Loading State
            assertEquals(SourcesState(loading = true, sources = emptyList()), awaitItem())
            // Items loaded by viewmodel
            assertEquals(SourcesState(loading = false, sources = mapJsonToSourcesState(jsonResponse)), awaitItem())
            // Items stored in local database
            assertEquals(true, localDataSource.getAllSources().isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun mapJsonToSourcesState(json: String): List<Source> {
        val jsonSerializer = Json {
            explicitNulls = false
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
        val sources = jsonSerializer.decodeFromString<SourcesResponse>(json).sources
        return mapSources(sources)
    }

}