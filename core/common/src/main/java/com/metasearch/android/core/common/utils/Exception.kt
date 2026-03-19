package com.metasearch.android.core.common.utils

import com.metasearch.android.core.common.R
import com.metasearch.android.core.common.constants.ErrorScope
import com.metasearch.android.core.common.extensions.isNetworkError
import retrofit2.HttpException

fun handleException(
    exception: Throwable,
    onError: (UiText) -> Unit,
) {
    when {
        exception is HttpException -> {
            when (exception.code()) {
                401 -> showErrorDialog(ErrorScope.IMAGE_ANALYSIS, exception)
                else -> {
                    onError(UiText.StringResource(R.string.error_server_with_code, exception.code()))
                }
            }
        }

        exception.isNetworkError() -> {
            onError(UiText.StringResource(R.string.error_network_unstable))
        }

        else -> {
            val message = exception.message
            if (message != null) {
                onError(UiText.DynamicString(message))
            } else {
                onError(UiText.StringResource(R.string.error_unknown))
            }
        }
    }
}
