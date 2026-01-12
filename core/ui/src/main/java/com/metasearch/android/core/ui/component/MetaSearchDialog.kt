package com.metasearch.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.component.MetaSearchButton
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.designsystem.theme.Rose
import com.metasearch.android.core.designsystem.theme.White

@Composable
fun MetaSearchDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmRequest: () -> Unit,
    dismissButtonText: String? = null,
    confirmButtonText: String,
    title: String? = null,
    content: @Composable (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        MetaSearchTheme.radius.lg,
                    ),
                )
                .background(White)
                .border(
                    width = MetaSearchTheme.border.border4,
                    color = LightPink,
                    shape = RoundedCornerShape(
                        MetaSearchTheme.radius.lg,
                    ),
                )
                .padding(
                    MetaSearchTheme.spacing.spacing6,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            title?.let {
                Text(
                    text = title,
                    color = Rose,
                    style = MetaSearchTheme.typography.titleLarge,
                )
            }
            Spacer(
                modifier = Modifier.height(
                    MetaSearchTheme.spacing.spacing4,
                ),
            )
            content?.let {
                Box(
                    modifier = Modifier.padding(
                        bottom = MetaSearchTheme.spacing.spacing6,
                    ),
                ) {
                    it()
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    MetaSearchTheme.spacing.spacing3,
                ),
            ) {
                dismissButtonText?.let {
                    MetaSearchButton(
                        modifier = Modifier.weight(1f),
                        text = it,
                        onClick = onDismissRequest,
                        contentColor = Neutral500,
                    )
                }

                MetaSearchButton(
                    modifier = Modifier.weight(1f),
                    text = confirmButtonText,
                    onClick = onConfirmRequest,
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun MetaSearchDialogPreview() {
    MetaSearchTheme {
        MetaSearchDialog(
            title = "권한 요청",
            content = {
                Text(
                    text = "앱을 이용하려면 권한 설정이 필요합니다.",
                )
            },
            onConfirmRequest = {},
            dismissButtonText = "닫기",
            confirmButtonText = "설정으로 이동",
        )
    }
}
