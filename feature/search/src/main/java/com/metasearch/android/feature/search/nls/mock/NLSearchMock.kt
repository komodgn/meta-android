package com.metasearch.android.feature.search.nls.mock

import com.metasearch.android.core.model.NLSearchResult
import com.metasearch.android.core.model.fake
import com.metasearch.android.feature.search.nls.NLSearchUiState
import kotlinx.collections.immutable.toPersistentList

fun NLSearchUiState.Companion.mock() = NLSearchUiState(
    isLoading = false,
    resultImages = NLSearchResult.fake().matchedUris.toPersistentList(),
    eventSink = {},
)
