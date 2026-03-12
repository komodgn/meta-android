package com.metasearch.android.feature.search.nls

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun NLSearchSideEffect(
    state: NLSearchUiState,
    eventSink: (NLSearchUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is NLSearchSideEffect.ShowToast -> {
                EventHandler.sendEvent(
                    MetaSearchEvent.ShowToast(
                        message = state.sideEffect.message.asString(context),
                    ),
                )
            }

            null -> {}
        }

        if (state.sideEffect != null) {
            eventSink(NLSearchUiEvent.InitSideEffect)
        }
    }
}
