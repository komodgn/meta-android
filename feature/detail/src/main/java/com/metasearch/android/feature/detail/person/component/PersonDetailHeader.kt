package com.metasearch.android.feature.detail.person.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral800
import com.metasearch.android.core.ui.component.MetaSearchDivider
import com.metasearch.android.feature.detail.R

@Composable
fun PersonDetailHeader(
    modifier: Modifier = Modifier,
    personName: String?,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
                .padding(MetaSearchTheme.spacing.spacing4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Arrow Back Icon",
                    tint = Neutral800,
                )
            }

            Text(
                modifier = Modifier.weight(1f),
                text = personName ?: "",
                style = MetaSearchTheme.typography.labelSmall,
                color = Neutral800,
                textAlign = TextAlign.Center,
            )

            IconButton(onClick = onMenuClick) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_menu_dots),
                    contentDescription = "More Menu",
                    tint = Neutral800,
                )
            }
        }
        MetaSearchDivider()
    }
}

@ComponentPreview
@Composable
fun PersonDetailHeaderPreview() {
    MetaSearchTheme {
        PersonDetailHeader(
            personName = "춘식이",
            onBackClick = {},
            onMenuClick = {},
        )
    }
}
