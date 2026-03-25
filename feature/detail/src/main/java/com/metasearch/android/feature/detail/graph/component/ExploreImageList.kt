package com.metasearch.android.feature.detail.graph.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.ui.component.MetaSearchSquareImage
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.graph.GraphDetailUiEvent
import com.metasearch.android.feature.detail.graph.GraphDetailUiState
import com.metasearch.android.feature.detail.graph.mock.mock

@Composable
fun ExploreImageList(
    state: GraphDetailUiState,
) {
    Column {
        Text(
            modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
            text = stringResource(R.string.graph_detail_screen_bottom_selected_image_label),
            color = Neutral500,
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            contentPadding = PaddingValues(horizontal = MetaSearchTheme.spacing.spacing4),
            horizontalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing2),
        ) {
            items(state.selectedImages) { uriString ->
                MetaSearchSquareImage(
                    model = uriString,
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { state.eventSink(GraphDetailUiEvent.OnImageClick(uriString)) },
                    onClick = { state.eventSink(GraphDetailUiEvent.OnImageClick(uriString)) },
                )
            }
        }
        Spacer(modifier = Modifier.height(MetaSearchTheme.spacing.spacing4))
    }
}

@ComponentPreview
@Composable
fun ExploreImageListPreview() {
    MetaSearchTheme {
        ExploreImageList(
            state = GraphDetailUiState.mock(),
        )
    }
}
