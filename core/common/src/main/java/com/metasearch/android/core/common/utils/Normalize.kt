package com.metasearch.android.core.common.utils

fun normalizePhoneNumber(phoneNumber: String): String {
    return phoneNumber.replace("[^0-9]".toRegex(), "")
}
