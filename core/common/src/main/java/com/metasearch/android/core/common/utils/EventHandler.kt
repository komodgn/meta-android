package com.metasearch.android.core.common.utils

import com.metasearch.android.core.common.constants.ErrorScope
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
    val description: String,
    val confirmText: String,
    val dismissText: String? = null,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit = {},
)

fun showErrorDialog(
    errorScope: ErrorScope,
    exception: Throwable,
    confirmText: String = "확인",
    onConfirm: () -> Unit = {},
) {
    val (title, message) = when {
        exception.isNetworkError() -> {
            null to "네트워크 연결이 불안정합니다.\n인터넷 연결을 확인해주세요."
        }

        exception is HttpException -> {
            when (errorScope) {
                ErrorScope.GLOBAL -> {
                    null to "알 수 없는 문제가 발생했습니다.\n잠시 후 다시 시도해주세요."
                }

                ErrorScope.IMAGE_ANALYSIS -> {
                    null to "이미지 분석 완료 후 다시 시도해주세요."
                }
            }
        }

        else -> {
            null to "알 수 없는 문제가 발생했습니다.\n잠시 후 다시 시도해주세요."
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
