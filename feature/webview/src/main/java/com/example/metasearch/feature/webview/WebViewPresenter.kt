package com.example.metasearch.feature.webview

import androidx.compose.runtime.Composable
import com.example.metasearch.feature.screens.WebViewScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent

class WebViewPresenter @AssistedInject constructor(
//    @Assisted private val navigator: Navigator,
) : Presenter<WebViewUiState> {
    @Composable
    override fun present(): WebViewUiState {
        fun handleEvent(event: WebViewUiEvent) = Unit

        return WebViewUiState(
            eventSink = ::handleEvent,
        )
    }

    @CircuitInject(WebViewScreen::class, ActivityRetainedComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
//            navigator: Navigator,
        ): WebViewPresenter
    }
}
