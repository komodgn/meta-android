package com.metasearch.android.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.Black
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.data.domain.Model
import com.metasearch.android.feature.home.R

@Composable
fun ModelItem(
    model: Model,
    isDownloading: Boolean,
    isInstalled: Boolean,
    downloadProgress: Float,
    onDownloadClick: (Model) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = MetaSearchTheme.spacing.spacing2)) {
        Text(
            text = model.name,
            style = MetaSearchTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(
                R.string.home_drawer_model_description,
                (model.sizeInBytes.toDouble() / (1024 * 1024 * 1024)).toFloat(),
            ),
            style = MetaSearchTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = MetaSearchTheme.spacing.spacing2)
        )

        if (isDownloading) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = MetaSearchTheme.spacing.spacing2)) {
                Text(text = stringResource(R.string.home_drawer_downloading_percent, (downloadProgress * 100).toInt()))
                LinearProgressIndicator(progress = { downloadProgress }, modifier = Modifier.fillMaxWidth())
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isDownloading && !isInstalled,
            onClick = { onDownloadClick(model) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isInstalled) Neutral500 else Black,
                contentColor = LightPink,
            )
        ) {
            Text(text = when {
                isInstalled -> stringResource(R.string.home_drawer_download_installed)
                isDownloading -> stringResource(R.string.home_drawer_download_in_progress)
                else -> stringResource(R.string.home_drawer_download_button, model.name)
            })
        }
    }
}

@ComponentPreview
@Composable
private fun ModelItemPreview() {
    MetaSearchTheme {
        ModelItem(
            model = Model(name = "Gemma-4-E2B-it", modelId = ""),
            isDownloading = false,
            isInstalled = false,
            downloadProgress = 0f,
            onDownloadClick = {},
        )
    }
}
