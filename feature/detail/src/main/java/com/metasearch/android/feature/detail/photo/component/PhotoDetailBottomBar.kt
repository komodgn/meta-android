package com.metasearch.android.feature.detail.photo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun PhotoDetailBottomBar(
    modifier: Modifier = Modifier,
    currentTab: PhotoDetailBottomBarItem? = null,
    onTabClick: (PhotoDetailBottomBarItem) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = MetaSearchTheme.spacing.spacing6,
                end = MetaSearchTheme.spacing.spacing6,
            )
            .clip(
                shape = RoundedCornerShape(
                    MetaSearchTheme.radius.lg,
                ),
            )
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
//                .navigationBarsPadding(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PhotoDetailBottomBarItem.entries.forEach { tab ->
                    BottomBarItem(
                        tab = tab,
                        isSelected = (tab == currentTab),
                        onClick = {
                            onTabClick(tab)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    tab: PhotoDetailBottomBarItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val color = if (isSelected) MetaSearchTheme.colors.brandContent else MetaSearchTheme.colors.brandContent.copy(alpha = 0.5f)

            Icon(
                painter = painterResource(tab.iconResId),
                contentDescription = "Tab Icon",
                tint = color,
            )
            Spacer(
                modifier = Modifier.height(MetaSearchTheme.spacing.spacing2),
            )
            Text(
                text = stringResource(tab.labelResId),
                color = color,
                style = MetaSearchTheme.typography.captionSmall,
            )
        }
    }
}

@ComponentPreview
@Composable
fun PhotoDetailBottomBarPreview() {
    MetaSearchTheme {
        PhotoDetailBottomBar(
            currentTab = PhotoDetailBottomBarItem.GRAPH,
            onTabClick = {},
        )
    }
}
