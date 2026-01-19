package com.metasearch.android.feature.search.nls.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.component.MetaSearchSearchBar
import com.metasearch.android.feature.search.R

@Composable
fun NLSearchTextField(
    modifier: Modifier = Modifier,
    inputString: String,
    onSearchClick: (String) -> Unit,
) {
    var localInput by remember(inputString) { mutableStateOf(inputString) }

    MetaSearchSearchBar(
        modifier = modifier,
        value = localInput,
        onValueChange = { localInput = it },
        onSearchClick = { onSearchClick(localInput) },
        placeholder = stringResource(R.string.nl_search_text_field_placeholder),
    )
}

@ComponentPreview
@Composable
private fun NLSearchTextFieldPreview() {
    MetaSearchTheme {
        NLSearchTextField(
            inputString = "서울에서 밤에 찍은 음식 사진",
            onSearchClick = {},
        )
    }
}
