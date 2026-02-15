package com.metasearch.android.feature.detail.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent

@Composable
fun PhotoDetailSideEffect(
    state: PhotoDetailUiState,
    eventSink: (PhotoDetailUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is PhotoDetailSideEffect.ShowToast -> {
                EventHandler.sendEvent(
                    MetaSearchEvent.ShowToast(
                        message = state.sideEffect.message.asString(context),
                    ),
                )
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(PhotoDetailUiEvent.InitSideEffect)
        }
    }
}
