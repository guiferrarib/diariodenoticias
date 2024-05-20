package com.ia.diariodenoticias.articles.presentation

import com.ia.diariodenoticias.app.BaseViewModel
import com.ia.diariodenoticias.articles.usecase.GetArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class ArticlesViewModel(
    private val useCase: GetArticlesUseCase
) : BaseViewModel() {

    private val _articlesState: MutableStateFlow<ArticlesState> =
        MutableStateFlow(ArticlesState(loading = true))

    val articlesState: StateFlow<ArticlesState> get() = _articlesState

    init {
        getArticles()
    }

    fun getArticles(forceFetch: Boolean = false) {
        scope.launch {
            _articlesState.emit(ArticlesState(loading = true, articles = _articlesState.value.articles))

            val fetchedArticles = useCase.getArticles(forceFetch)

            _articlesState.emit(ArticlesState(articles = fetchedArticles))
        }
    }
}