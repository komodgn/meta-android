package com.metasearch.android.feature.person.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.feature.person.R

@Composable
fun PersonSearchTextField(
    modifier: Modifier = Modifier,
    inputString: String,
    onInputChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MetaSearchTheme.spacing.spacing4)
            .border(
                width = 1.dp,
                color = Neutral500,
                shape = RoundedCornerShape(
                    MetaSearchTheme.radius.full,
                ),
            )
            .padding(
                horizontal = MetaSearchTheme.spacing.spacing4,
                vertical = MetaSearchTheme.spacing.spacing3,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = inputString,
            onValueChange = onInputChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MetaSearchTheme.typography.bodyLarge,
            decorationBox = { innerTextField ->
                if (inputString.isEmpty()) {
                    Text(
                        text = stringResource(R.string.person_search_text_field_placeholder),
                        color = Neutral500,
                        style = MetaSearchTheme.typography.bodyLarge,
                    )
                }
                innerTextField()
            },
        )

        Icon(
            painter = painterResource(R.drawable.ic_search_line),
            contentDescription = "Search Icon",
            modifier = Modifier
                .size(
                    MetaSearchTheme.spacing.spacing6,
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    onSearchClick()
                },
            tint = Neutral500,
        )
    }
}

@ComponentPreview
@Composable
private fun PersonSearchTextFieldPreview() {
    MetaSearchTheme {
        PersonSearchTextField(
            inputString = "춘식이",
            onSearchClick = {},
            onInputChange = {},
        )
    }
}
