package com.example.metasearch.feature.search.nls

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
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.ui.MetaSearchScaffold
import com.example.metasearch.core.ui.component.MetaSearchDialog
import com.example.metasearch.core.ui.component.MetaSearchLoadingIndicator
import com.example.metasearch.core.ui.component.MetaSearchSquareImage
import com.example.metasearch.feature.screens.NLSearchScreen
import com.example.metasearch.feature.screens.component.MetaSearchMainBottomBar
import com.example.metasearch.feature.screens.component.MetaSearchMainTabItem
import com.example.metasearch.feature.search.R
import com.example.metasearch.feature.search.nls.component.NLSearchHeader
import com.example.metasearch.feature.search.nls.component.NLSearchTextField
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(NLSearchScreen::class, ActivityRetainedComponent::class)
@Composable
fun NLSearchUi(
    modifier: Modifier = Modifier,
    state: NLSearchUiState,
) {
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
            NLSearchHeader()

            NLSearchTextField(
                modifier = modifier,
                inputString = state.inputString,
                onInputChange = {
                    state.eventSink(NLSearchUiEvent.OnInputChange(it))
                },
                onSearchClick = {
                    state.eventSink(NLSearchUiEvent.OnNLSearchClick(state.inputString))
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

            if (state.errorMessage.isNotBlank()) {
                MetaSearchDialog(
                    title = stringResource(R.string.nl_search_screen_error_dialog_title),
                    content = {
                        Text(
                            text = state.errorMessage,
                        )
                    },
                    onDismissRequest = {
                        state.eventSink(NLSearchUiEvent.OnDialogCloseButtonClick)
                    },
                    dismissButtonText = stringResource(R.string.nl_search_screen_dialog_close_button),
                )
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
