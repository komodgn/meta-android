package com.metasearch.android.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.extensions.shareImage
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun HomeSideEffect(
    state: HomeUiState,
    eventSink: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is HomeSideEffect.ShareImage -> {
                context.shareImage(state.sideEffect.uriString)
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(HomeUiEvent.InitSideEffect)
        }
    }
}
