package com.metasearch.android.core.common.utils

import com.metasearch.android.core.common.R
import com.metasearch.android.core.common.constants.ErrorScope
import com.metasearch.android.core.common.extensions.isNetworkError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import retrofit2.HttpException

object EventHandler {
    private val _eventFlow = Channel<MetaSearchEvent>(Channel.BUFFERED)
    val eventFlow = _eventFlow.receiveAsFlow()

    fun sendEvent(event: MetaSearchEvent) {
        _eventFlow.trySend(event)
    }
}

sealed interface MetaSearchEvent {
    data class ShowDialog(
        val dialogSpec: MetaSearchDialogSpec,
    ) : MetaSearchEvent

    data class ShowToast(
        val message: String,
    ) : MetaSearchEvent
}

data class MetaSearchDialogSpec(
    val title: String? = null,
    val description: UiText,
    val confirmText: UiText = UiText.StringResource(R.string.confirm),
    val dismissText: UiText? = null,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit = {},
)

fun showErrorDialog(
    errorScope: ErrorScope,
    exception: Throwable,
    confirmText: UiText = UiText.StringResource(R.string.confirm),
    onConfirm: () -> Unit = {},
) {
    val (title, message) = when {
        exception.isNetworkError() -> {
            null to UiText.StringResource(R.string.error_network_unstable)
        }

        exception is HttpException -> {
            when (errorScope) {
                ErrorScope.GLOBAL -> {
                    null to UiText.StringResource(R.string.error_unknown)
                }

                ErrorScope.IMAGE_ANALYSIS -> {
                    null to UiText.StringResource(R.string.error_wait_analysis)
                }
            }
        }

        else -> {
            null to UiText.StringResource(R.string.error_unknown)
        }
    }

    val spec = MetaSearchDialogSpec(
        title = title,
        description = message,
        confirmText = confirmText,
        onConfirm = onConfirm,
    )

    EventHandler.sendEvent(event = MetaSearchEvent.ShowDialog(spec))
}
