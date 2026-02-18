package com.metasearch.android.feature.detail.photo

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
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

            is PhotoDetailSideEffect.ShareImage -> {
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
            eventSink(PhotoDetailUiEvent.InitSideEffect)
        }
    }
}
