package com.metasearch.android.feature.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent

@Composable
fun GraphSideEffect(
    state: GraphUiState,
    eventSink: (GraphUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
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
