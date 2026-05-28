package com.metasearch.android.core.designsystem.component

import android.os.Build
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
) {
    val isRobolectric = Build.FINGERPRINT.contains("robolectric", ignoreCase = true)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(MetaSearchTheme.radius.full),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MetaSearchTheme.colors.contentPrimary,
            unfocusedTextColor = MetaSearchTheme.colors.contentPrimary,
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = if (isRobolectric) Color.Transparent else MaterialTheme.colorScheme.tertiary,
        ),
    )
}

@ComponentPreview
@Composable
private fun MetaSearchTextFieldPreview() {
    MetaSearchTheme {
        MetaSearchTextField(
            value = "",
            onValueChange = {},
            label = "이름",
        )
    }
}

@ComponentPreview
@Composable
private fun MetaSearchTextFieldFilledPreview() {
    MetaSearchTheme {
        MetaSearchTextField(
            value = "명수",
            onValueChange = {},
            label = "이름",
        )
    }
}
