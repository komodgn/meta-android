package com.metasearch.android.feature.photo_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.extensions.shareImage
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun PhotoDetailSideEffect(
    state: PhotoDetailUiState,
    eventSink: (PhotoDetailUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is PhotoDetailSideEffect.ShowToast -> {
                EventHandler.sendEvent(
                    MetaSearchEvent.ShowToast(
                        message = state.sideEffect.message.asString(context),
                    ),
                )
            }

            is PhotoDetailSideEffect.ShareImage -> {
                context.shareImage(state.sideEffect.uriString)
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(PhotoDetailUiEvent.InitSideEffect)
        }
    }
}