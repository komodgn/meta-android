package com.metasearch.android.feature.main

import android.net.Uri
import com.metasearch.android.core.testing.annotation.RunWith
import com.metasearch.android.feature.main.deeplink.DeepLinkParser
import com.metasearch.android.feature.screens.GraphScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkParserTest {

    @Test
    fun `should return GraphScreen when graph deeplink is received`() {
        val uri = Uri.parse("metasearch://graph")
        val result = DeepLinkParser.parse(uri)

        assertEquals(1, result?.size)
        assertTrue(result!![0] is GraphScreen)
    }
}
