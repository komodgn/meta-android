package com.metasearch.android.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun GraphSideEffect(
    state: GraphUiState,
    eventSink: (GraphUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is GraphSideEffect.ShowToast -> {
                EventHandler.sendEvent(MetaSearchEvent.ShowToast(state.sideEffect.message.asString(context)))
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(GraphUiEvent.InitSideEffect)
        }
    }
}
