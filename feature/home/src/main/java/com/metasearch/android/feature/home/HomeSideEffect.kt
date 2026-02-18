package com.metasearch.android.feature.home

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun HomeSideEffect(
    state: HomeUiState,
    eventSink: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is HomeSideEffect.ShareImage -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, state.sideEffect.uriString.toUri())
                    type = "image/*"
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }

            else -> {}
        }

        if (state.sideEffect != null) {
            eventSink(HomeUiEvent.InitSideEffect)
        }
    }
}
