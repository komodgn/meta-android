package com.metasearch.android.feature.detail.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.MetaSearchEvent

@Composable
fun GraphDetailSideEffect(
    state: GraphDetailUiState,
    eventSink: (GraphDetailUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
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
