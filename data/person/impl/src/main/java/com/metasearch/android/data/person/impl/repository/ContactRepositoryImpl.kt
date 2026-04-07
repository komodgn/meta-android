package com.metasearch.android.data.person.impl.repository

import android.content.Context
import android.provider.CallLog
import com.metasearch.android.core.common.utils.normalizePhoneNumber
import com.metasearch.android.core.di.scope.DataScope
import com.metasearch.android.domain.person.api.repository.ContactRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@SingleIn(DataScope::class)
@Inject
class ContactRepositoryImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ContactRepository {

    override suspend fun getCallDurations(): Map<String, Long> = withContext(ioDispatcher) {
        val callLogDuration = mutableMapOf<String, Long>()

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DURATION),
            null,
            null,
            null,
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            if (numberIndex >= 0 && durationIndex >= 0) {
                while (it.moveToNext()) {
                    val number = it.getString(numberIndex)
                    val duration = it.getLong(durationIndex)
                    val normalizedNumber = normalizePhoneNumber(number)

                    callLogDuration[normalizedNumber] =
                        callLogDuration.getOrDefault(normalizedNumber, 0L) + duration
                }
            }
        }
        callLogDuration
    }
}
