package com.metasearch.android.feature.person.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.Black
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.model.PersonModel
import com.metasearch.android.core.ui.component.MetaSearchCircleImage
import com.metasearch.android.feature.person.R

@Composable
internal fun PersonItem(
    person: PersonModel,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayImage = remember(person) {
        person.faces.find { it.id == person.representativeFaceId }?.imageData
            ?: person.faces.firstOrNull()?.imageData
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() }
            .padding(MetaSearchTheme.spacing.spacing2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MetaSearchCircleImage(
                model = displayImage,
                contentDescription = person.inputName,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_cross_circle),
                contentDescription = "Delete Icon",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(26.dp)
                    .padding(MetaSearchTheme.spacing.spacing1)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onDeleteClick()
                    },
                tint = Black.copy(alpha = 0.7f),
            )
        }

        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing2))

        Text(
            text = person.inputName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ComponentPreview
@Composable
private fun PersonItemPreview() {
    MetaSearchTheme {
        PersonItem(
            person = PersonModel(
                id = 1L,
                name = "춘식이",
                inputName = "춘식이",
            ),
            onDeleteClick = {},
            onClick = {},
        )
    }
}
