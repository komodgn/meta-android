package com.example.metasearch.feature.person.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.example.metasearch.core.designsystem.annotation.ComponentPreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral200
import com.example.metasearch.core.model.PersonModel

@Composable
internal fun PersonItem(
    person: PersonModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(MetaSearchTheme.spacing.spacing2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = person.faces.firstOrNull()?.imageData,
            contentDescription = person.inputName,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Neutral200),
            contentScale = ContentScale.Crop,
        )

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
            onClick = {},
        )
    }
}
