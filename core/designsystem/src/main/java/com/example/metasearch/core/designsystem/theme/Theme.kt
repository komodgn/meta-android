package com.example.metasearch.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MetaSearchTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        content = content,
    )
}

object MetaSearchTheme {

}
