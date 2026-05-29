package com.metasearch.android.feature.graph_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun GraphDetailSideEffect(
    state: GraphDetailUiState,
    eventSink: (GraphDetailUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is GraphDetailSideEffect.ShowToast -> {
                MetaSearchEvent.ShowToast(message = state.sideEffect.message.asString(context))
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(GraphDetailUiEvent.InitSideEffect)
        }
    }
}
