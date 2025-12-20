package com.example.metasearch.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.Black
import com.example.metasearch.core.designsystem.theme.LightPink
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(1000f)
            .background(Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = LightPink,
        )
    }
}

@ComponentPreview
@Composable
private fun MetaSearchLoadingIndicatorPreview() {
    MetaSearchTheme {
        MetaSearchLoadingIndicator()
    }
}
