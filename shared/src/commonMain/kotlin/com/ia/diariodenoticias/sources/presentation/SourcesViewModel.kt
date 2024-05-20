package com.ia.diariodenoticias.sources.presentation

import com.ia.diariodenoticias.app.BaseViewModel
import com.ia.diariodenoticias.sources.domain.GetSourcesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
/**
 * Copyright (c) 2024
 * Todos os direitos reservados.
 *
 * Autor: Guilherme Ferrari Bréscia
 */

class SourcesViewModel(private val useCase: GetSourcesUseCase) : BaseViewModel() {

    private val _sourcesState =
        MutableStateFlow(SourcesState(listOf(), true, null))
    val sourcesState: StateFlow<SourcesState> get() = _sourcesState

    init {
        getSources()
    }

    fun getSources() {
        scope.launch {
            _sourcesState.emit(SourcesState(_sourcesState.value.sources, true, null))

            val sources = useCase.getSources()

            _sourcesState.emit(
                SourcesState(sources)
            )
        }
    }
}