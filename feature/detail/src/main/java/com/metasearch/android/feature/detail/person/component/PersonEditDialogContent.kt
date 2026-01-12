package com.metasearch.android.feature.detail.person.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.component.MetaSearchTextField
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.feature.detail.R
import com.metasearch.android.feature.detail.person.PersonDetailUiEvent

@Composable
internal fun PersonEditDialogContent(
    editName: String,
    editPhone: String,
    editIsHomeDisplay: Boolean,
    onEvent: (PersonDetailUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MetaSearchTheme.spacing.spacing3),
    ) {
        MetaSearchTextField(
            value = editName,
            onValueChange = { onEvent(PersonDetailUiEvent.OnEditNameChange(it)) },
            label = stringResource(R.string.person_detail_screen_person_name_placeholder),
            modifier = Modifier.fillMaxWidth(),
        )

        MetaSearchTextField(
            value = editPhone,
            onValueChange = { onEvent(PersonDetailUiEvent.OnEditPhoneChange(it)) },
            label = stringResource(R.string.person_detail_screen_phone_number_placeholder),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onEvent(PersonDetailUiEvent.OnEditHomeDisplayChange(!editIsHomeDisplay)) }
                .padding(vertical = MetaSearchTheme.spacing.spacing1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = editIsHomeDisplay,
                onCheckedChange = { onEvent(PersonDetailUiEvent.OnEditHomeDisplayChange(it)) },
            )
            Text(text = stringResource(R.string.person_detail_screen_is_home_display_label))
        }
    }
}

@ComponentPreview
@Composable
private fun PersonEditContentPreview() {
    MetaSearchTheme {
        PersonEditDialogContent(
            editName = "춘식이",
            editPhone = "01077771212",
            editIsHomeDisplay = true,
            onEvent = {},
        )
    }
}
