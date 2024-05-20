package com.ia.diariodenoticias.sources.data

import com.ia.diariodenoticias.sources.data.impl.SourcesRepositoryImpl
import com.ia.diariodenoticias.sources.data.model.SourceRaw
import com.ia.diariodenoticias.sources.usecase.Source
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
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */
class SourcesRepositoryTest : KoinTest {

    //For coverage run ./gradlew :shared:koverHtmlReportDebug

    private val dispatcherProvider = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                module {
                    single<SourcesRepository> { SourcesRepositoryImpl(get()) }
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
    fun `Given an HTTP client When fetching top headlines sources Then the response should match the expected list of sources`() = testApplication {
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
                    get("/v2/top-headlines/sources") {
                        call.respond(mountListOfSources())
                    }
                }
            }
        }
        val sourcesRepository = SourcesRepositoryImpl(FakeSourcesDataSourceImpl(testHttpClient))

        assertEquals(mountListOfSources(), sourcesRepository.getAllSources())
    }

    @Test
    fun `Given an HTTP client When fetching top headlines sources Then the response should not be an empty list`() = testApplication {
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
                    get("/v2/top-headlines/sources") {
                        call.respond(mountListOfSources())
                    }
                }
            }
        }
        val sourcesRepository = SourcesRepositoryImpl(FakeSourcesDataSourceImpl(testHttpClient))

        assertNotEquals(emptyList(), sourcesRepository.getAllSources())
    }

    private fun mountListOfSources(): List<SourceRaw> = listOf(
        SourceRaw("1","New York Times1", "desc1", "language1", "country1"),
        SourceRaw("2","New York Times2", "desc2", "language2", "country2"),
        SourceRaw("3","New York Times3", "desc3", "language3", "country3"),
        SourceRaw("4","New York Times4", "desc4", "language4", "country4"),
        SourceRaw("5","New York Times5", "desc5", "language5", "country5"),
    )

    inner class FakeSourcesDataSourceImpl(private val httpClient: HttpClient) : SourcesDataSource {
        override fun getAllSources(): List<SourceRaw> {
            return emptyList()
        }

        override fun clearSources() {
            return
        }

        override fun createSources(sources: List<SourceRaw>) {
            return
        }

        override fun insertSource(source: SourceRaw) {
            return
        }

        override suspend fun fetchSources(): List<SourceRaw> {
            return httpClient.get("/v2/top-headlines/sources") {
                header("hosts", "localhost")
            }.body()
        }

        override fun mapSource(
            id: String,
            name: String,
            desc: String,
            language: String,
            country: String
        ): SourceRaw {
            return SourceRaw(
                id,
                name,
                desc,
                language,
                country
            )
        }

    }

}