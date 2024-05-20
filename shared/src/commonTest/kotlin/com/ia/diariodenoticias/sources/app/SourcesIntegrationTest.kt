package com.ia.diariodenoticias.sources.app

import app.cash.turbine.test
import com.ia.diariodenoticias.app.di.sharedKoinModules
import com.ia.diariodenoticias.sources.data.SourcesDataSource
import com.ia.diariodenoticias.sources.data.model.SourcesResponse
import com.ia.diariodenoticias.sources.presentation.SourcesState
import com.ia.diariodenoticias.sources.presentation.SourcesViewModel
import com.ia.diariodenoticias.sources.domain.Source
import com.ia.diariodenoticias.utils.JsonLoader
import com.ia.diariodenoticias.utils.MockClient
import com.ia.diariodenoticias.utils.MockResponse
import com.ia.diariodenoticias.utils.di.stopTestKoin
import com.ia.diariodenoticias.utils.di.testPlatformModule
import com.ia.diariodenoticias.utils.mapSources
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