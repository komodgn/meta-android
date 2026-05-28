package com.metasearch.android.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.data.domain.Model
import com.metasearch.android.feature.home.R

@Composable
fun ModelItem(
    model: Model,
    isDownloading: Boolean,
    isInstalled: Boolean,
    downloadProgress: Float,
    onDownloadClick: (Model) -> Unit,
    onDeleteClick: (Model) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = MetaSearchTheme.spacing.spacing2),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = model.name,
                style = MetaSearchTheme.typography.bodyLarge,
                color = MetaSearchTheme.colors.contentPrimary,
                modifier = Modifier.weight(1f),
            )

            if (isInstalled) {
                IconButton(onClick = { onDeleteClick(model) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_trash_xmark),
                        contentDescription = "Delete Model",
                        tint = MetaSearchTheme.colors.contentSecondary,
                    )
                }
            }
        }
        Text(
            text = stringResource(
                R.string.home_drawer_model_description,
                (model.sizeInBytes.toDouble() / (1024 * 1024 * 1024)).toFloat(),
            ),
            style = MetaSearchTheme.typography.bodyMedium,
            color = MetaSearchTheme.colors.contentSecondary,
            modifier = Modifier.padding(vertical = MetaSearchTheme.spacing.spacing2),
        )

        if (isDownloading) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = MetaSearchTheme.spacing.spacing2)) {
                Text(text = stringResource(R.string.home_drawer_downloading_percent, (downloadProgress * 100).toInt()))
                LinearProgressIndicator(
                    progress = { downloadProgress },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isDownloading && !isInstalled,
            onClick = { onDownloadClick(model) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MetaSearchTheme.colors.actionPrimary,
                contentColor = MetaSearchTheme.colors.actionContent,
                disabledContainerColor = MetaSearchTheme.colors.surfaceVariant,
                disabledContentColor = MetaSearchTheme.colors.contentSecondary,
            ),
        ) {
            Text(
                text = when {
                    isInstalled -> stringResource(R.string.home_drawer_download_installed)
                    isDownloading -> stringResource(R.string.home_drawer_download_in_progress)
                    else -> stringResource(R.string.home_drawer_download_button, model.name)
                },
            )
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
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun ModelItemInstalledPreview() {
    MetaSearchTheme {
        ModelItem(
            model = Model(name = "Gemma-4-E2B-it", modelId = ""),
            isDownloading = false,
            isInstalled = true,
            downloadProgress = 0f,
            onDownloadClick = {},
            onDeleteClick = {},
        )
    }
}

@ComponentPreview
@Composable
private fun ModelItemDownLoadingPreview() {
    MetaSearchTheme {
        ModelItem(
            model = Model(name = "Gemma-4-E2B-it", modelId = ""),
            isDownloading = true,
            isInstalled = false,
            downloadProgress = 0f,
            onDownloadClick = {},
            onDeleteClick = {},
        )
    }
}
