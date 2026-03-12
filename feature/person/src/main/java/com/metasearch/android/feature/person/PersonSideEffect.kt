package com.metasearch.android.feature.person

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.skydoves.compose.effects.RememberedEffect

@Composable
fun PersonSideEffect(
    state: PersonUiState,
    eventSink: (PersonUiEvent) -> Unit,
) {
    val context = LocalContext.current

    RememberedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is PersonSideEffect.ShowToast -> {
                EventHandler.sendEvent(
                    MetaSearchEvent.ShowToast(
                        message = state.sideEffect.message.asString(context),
                    ),
                )
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(PersonUiEvent.InitSideEffect)
        }
    }
}
