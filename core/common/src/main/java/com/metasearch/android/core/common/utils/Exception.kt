package com.metasearch.android.core.common.utils

import com.metasearch.android.core.common.constants.ErrorScope
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun handleException(
    exception: Throwable,
    onError: (String) -> Unit,
) {
    when {
        exception is HttpException -> {
            when (exception.code()) {
                401 -> showErrorDialog(ErrorScope.IMAGE_ANALYSIS, exception)
                else -> {
                    val message = "서버 오류가 발생했습니다. (${exception.code()})"
                    onError(message)
                }
            }
        }

        exception.isNetworkError() -> {
            onError("네트워크 연결이 불안정합니다. 잠시 후 다시 시도해주세요.")
        }

        else -> {
            val message = exception.message ?: "문제가 발생했습니다. 잠시 후 다시 시도해주세요."
            onError(message)
        }
    }
}

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectException ||
        this is SocketTimeoutException ||
        this is IOException
}
