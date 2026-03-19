package com.metasearch.android.core.common.extensions

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectException ||
        this is SocketTimeoutException ||
        this is IOException
}
