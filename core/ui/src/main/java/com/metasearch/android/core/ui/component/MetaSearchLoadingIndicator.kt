package com.metasearch.android.core.ui.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.zIndex
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

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
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        if (isInspectionMode || isRobolectric) {
            CircularProgressIndicator(
                progress = { 0.75f },
                color = MetaSearchTheme.colors.contentSecondary.copy(0.5f),
            )
        } else {
            CircularProgressIndicator(
                color = MetaSearchTheme.colors.contentSecondary.copy(0.5f),
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
