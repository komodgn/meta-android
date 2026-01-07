package com.example.metasearch.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.LightGrey
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchDivider(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.fillMaxWidth()
            .height(MetaSearchTheme.spacing.spacing05)
            .background(LightGrey),
    )
}

@ComponentPreview
@Composable
private fun MetaSearchDividerPreview() {
    MetaSearchTheme {
        MetaSearchDivider()
    }
}
