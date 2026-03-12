package com.metasearch.android.feature.search.focusing

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun FocusingSearchSideEffect(
    state: FocusingSearchUiState,
    eventSink: (FocusingSearchUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is FocusingSearchSideEffect.ShowToast -> {
                EventHandler.sendEvent(
                    MetaSearchEvent.ShowToast(
                        message = state.sideEffect.message.asString(context),
                    ),
                )
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(FocusingSearchUiEvent.InitSideEffect)
        }
    }
}
