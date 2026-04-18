package com.metasearch.android.core.ui.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.zIndex
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.Black
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral300

@Composable
fun MetaSearchLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    val isRobolectric = Build.FINGERPRINT.contains("robolectric", ignoreCase = true)
    val isInspectionMode = LocalInspectionMode.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(1000f)
            .background(Black.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        if (isInspectionMode || isRobolectric) {
            CircularProgressIndicator(
                progress = { 0.75f },
                color = Neutral300,
            )
        } else {
            CircularProgressIndicator(
                color = Neutral300,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun MetaSearchLoadingIndicatorPreview() {
    MetaSearchTheme {
        MetaSearchLoadingIndicator()
    }
}
