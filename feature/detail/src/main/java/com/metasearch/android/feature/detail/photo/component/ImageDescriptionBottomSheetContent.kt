package com.metasearch.android.feature.detail.photo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.feature.detail.R

@Composable
internal fun ImageDescriptionBottomSheetContent(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MetaSearchTheme.spacing.spacing5)
            .padding(bottom = MetaSearchTheme.spacing.spacing10),
    ) {
        Text(
            text = stringResource(R.string.photo_detail_screen_openai_bottom_sheet_title),
        )
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
        Text(
            text = description,
            style = MetaSearchTheme.typography.bodyLarge,
            color = MetaSearchTheme.colors.contentSecondary,
        )
    }
}

@Composable
@ComponentPreview
private fun ImageDescriptionBottomSheetContentPreview() {
    MetaSearchTheme {
        ImageDescriptionBottomSheetContent(
            description = "2026년 1월에 촬영된 이 사진은 ",
        )
    }
}
