package com.metasearch.android.data.person.impl

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import androidx.test.core.app.ApplicationProvider
import com.metasearch.android.core.common.utils.normalizePhoneNumber
import com.metasearch.android.data.person.impl.repository.ContactRepositoryImpl
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContactRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var repository: ContactRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()
    private val insertedUris = mutableListOf<Uri>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.contentResolver.delete(CallLog.Calls.CONTENT_URI, null, null)
        repository = ContactRepositoryImpl(context, testDispatcher)
    }

    @After
    fun tearDown() {
        insertedUris.forEach { uri ->
            context.contentResolver.delete(uri, null, null)
        }
        insertedUris.clear()
    }

    @Test
    fun `getCallDurations - sums up durations for the same normalized number`() = runTest(testDispatcher) {
        // given
        insertMockCallLog(number = "010-1234-5678", duration = 100L)
        insertMockCallLog(number = "01012345678", duration = 200L)

        // when
        val result = repository.getCallDurations()

        // then
        val normalized = normalizePhoneNumber("01012345678")
        assertEquals(300L, result[normalized])
        assertEquals(1, result.size)
    }

    private fun insertMockCallLog(number: String, duration: Long) {
        val values = ContentValues().apply {
            put(CallLog.Calls.NUMBER, number)
            put(CallLog.Calls.DURATION, duration)
            put(CallLog.Calls.DATE, System.currentTimeMillis())
            put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE)
        }
        val insertedUri = context.contentResolver.insert(CallLog.Calls.CONTENT_URI, values)

        assertNotNull("Failed to insert", insertedUri)
        insertedUri?.let { insertedUris.add(it) }
    }
}
