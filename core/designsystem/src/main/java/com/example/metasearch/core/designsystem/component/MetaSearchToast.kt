package com.example.metasearch.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightPink
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchToast(
    modifier: Modifier = Modifier,
    message: String? = null,
    isVisible: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(MetaSearchTheme.radius.full),
                modifier = modifier.padding(horizontal = MetaSearchTheme.spacing.spacing8),
            ) {
                message?.let {
                    Text(
                        text = it,
                        color = LightPink,
                        style = MetaSearchTheme.typography.captionSmall,
                        modifier = Modifier.padding(
                            horizontal = MetaSearchTheme.spacing.spacing4,
                            vertical = MetaSearchTheme.spacing.spacing2,
                        ),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MetaSearchToastPreview() {
    MetaSearchTheme {
        MetaSearchToast(
            isVisible = true,
            message = "드래그 해서 원을 그려주세요.",
        )
    }
}
