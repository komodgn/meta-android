package com.metasearch.android.feature.search.focusing.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.metasearch.android.core.common.extensions.previewPlaceholder
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.model.SearchResult

@Composable
internal fun SearchResultList(
    modifier: Modifier = Modifier,
    result: SearchResult,
    onImageClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MetaSearchTheme.spacing.spacing4),
        verticalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing4),
        contentPadding = PaddingValues(vertical = MetaSearchTheme.spacing.spacing4),
    ) {
        val displayLimit = 3

        items(result.groups) { group ->
            Column {
                Text(
                    text = group.categoryName,
                    color = Neutral500,
                    modifier = Modifier.padding(bottom = MetaSearchTheme.spacing.spacing2),
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
                ) {
                    val itemsToShow = group.photoNames.take(displayLimit)
                    val hasMore = group.photoNames.size > displayLimit

                    items(itemsToShow) { photoName ->
                        AsyncImage(
                            model = photoName,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .previewPlaceholder()
                                .clip(RoundedCornerShape(MetaSearchTheme.radius.sm))
                                .clickable {
                                    onImageClick(photoName)
                                },
                            contentScale = ContentScale.Crop,
                        )
                    }

                    if (hasMore) {
                        item {
                            MoreButton(
                                onClick = { onImageClick(group.categoryName) },
                            )
                        }
                    }
                }
            }
        }
    }
}
