package com.example.metasearch.feature.detail.photo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.feature.detail.R

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
        )
    }
}
