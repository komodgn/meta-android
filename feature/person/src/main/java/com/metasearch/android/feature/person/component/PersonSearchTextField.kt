package com.metasearch.android.feature.person.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.component.MetaSearchSearchBar
import com.metasearch.android.feature.person.R

@Composable
fun PersonSearchTextField(
    modifier: Modifier = Modifier,
    inputString: String,
    onInputChange: (String) -> Unit,
) {
    MetaSearchSearchBar(
        modifier = modifier,
        value = inputString,
        onValueChange = onInputChange,
        onSearchClick = { },
        placeholder = stringResource(R.string.person_search_text_field_placeholder),
    )
}

@ComponentPreview
@Composable
private fun PersonSearchTextFieldPreview() {
    MetaSearchTheme {
        PersonSearchTextField(
            inputString = "춘식이",
            onInputChange = {},
        )
    }
}
