package com.metasearch.android.feature.search.nls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.ui.MetaSearchScaffold
import com.metasearch.android.core.ui.component.MetaSearchHeader
import com.metasearch.android.core.ui.component.MetaSearchLoadingIndicator
import com.metasearch.android.core.ui.component.MetaSearchSquareImage
import com.metasearch.android.feature.screens.NLSearchScreen
import com.metasearch.android.feature.screens.component.MetaSearchMainBottomBar
import com.metasearch.android.feature.screens.component.MetaSearchMainTabItem
import com.metasearch.android.feature.search.R
import com.metasearch.android.feature.search.nls.component.NLSearchTextField
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
@Composable
fun NLSearchUi(
    modifier: Modifier = Modifier,
    state: NLSearchUiState,
) {
    NLSearchSideEffect(
        state = state,
        eventSink = state.eventSink,
    )

    MetaSearchScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MetaSearchMainBottomBar(
                modifier = modifier,
                currentTab = MetaSearchMainTabItem.SEARCH,
                onTabSelected = {
                    state.eventSink(NLSearchUiEvent.OnTabClick(it.screen))
                },
            )
        },
    ) { innerPadding ->
        NLSearchUiContent(
            modifier = modifier,
            state = state,
            innerPadding = innerPadding,
        )
    }
}

@Composable
private fun NLSearchUiContent(
    modifier: Modifier = Modifier,
    state: NLSearchUiState,
    innerPadding: PaddingValues,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            MetaSearchHeader(
                title = stringResource(R.string.nl_search_screen_header),
                textAlign = TextAlign.Start,
                textStyle = MetaSearchTheme.typography.headlineSmall,
            )

            NLSearchTextField(
                modifier = modifier,
                inputString = state.inputString,
                onSearchClick = { lastInput ->
                    state.eventSink(NLSearchUiEvent.OnInputChange(lastInput))
                    state.eventSink(NLSearchUiEvent.OnNLSearchClick(lastInput))
                },
            )

            Text(
                modifier = Modifier.padding(MetaSearchTheme.spacing.spacing2),
                text = stringResource(R.string.nl_search_screen_result_label),
                color = Neutral500,
            )
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(5),
                ) {
                    items(state.resultImages) { uriString ->
                        MetaSearchSquareImage(
                            model = uriString,
                            onClick = {
                                state.eventSink(NLSearchUiEvent.OnImageClick(uriString))
                            },
                        )
                    }
                }

                if (state.isLoading) {
                    MetaSearchLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun NLSearchUiPreview() {
    MetaSearchTheme {
        NLSearchUi(
            state = NLSearchUiState(
                isLoading = false,
                resultImages = listOf(
                    "uri1",
                    "uri2",
                ),
                eventSink = {},
            ),
        )
    }
}
