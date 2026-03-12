package com.metasearch.android.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.shareImage(uriString: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uriString.toUri())
        type = "image/*"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    startActivity(Intent.createChooser(sendIntent, null))
}
