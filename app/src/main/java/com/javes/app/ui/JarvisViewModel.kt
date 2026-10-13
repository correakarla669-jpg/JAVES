package com.javes.app.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Guarda o estado atual do J.A.V.E.S.
 * Nas próximas etapas, fala, escuta, palmas e IA vão alterar este estado.
 */
class JarvisViewModel : ViewModel() {

    private val _state = MutableStateFlow(JarvisState.IDLE)
    val state: StateFlow<JarvisState> = _state.asStateFlow()

    fun setState(newState: JarvisState) {
        _state.value = newState
    }
}
