package com.example.metasearch.feature.screens.component

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.Black
import com.example.metasearch.core.designsystem.theme.LightPink
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral800
import com.example.metasearch.core.designsystem.theme.White
import com.example.metasearch.core.ui.MetaSearchScaffold

@Composable
fun MetaSearchMainBottomBar(
    modifier: Modifier = Modifier,
    currentTab: MetaSearchMainTabItem,
    onTabSelected: (MetaSearchMainTabItem) -> Unit,
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
            .background(Black.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .navigationBarsPadding(),
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Neutral800),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MetaSearchMainTabItem.entries.forEach { tab ->
                    BottomBarItem(
                        tab = tab,
                        isSelected = (tab == currentTab),
                        onClick = {
                            onTabSelected(tab)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    tab: MetaSearchMainTabItem,
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
            val color = if (isSelected) LightPink else White

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
private fun MetaSearchMainBottomBarPreview() {
    MetaSearchTheme {
        MetaSearchScaffold(
            bottomBar = {
                MetaSearchMainBottomBar(
                    currentTab = MetaSearchMainTabItem.HOME,
                    onTabSelected = { },
                )
            },
        ) { }
    }
}
