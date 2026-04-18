package com.metasearch.android.core.designsystem.component

import android.os.Build
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.LightPink
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Rose

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
            focusedBorderColor = Rose,
            unfocusedBorderColor = LightPink,
            focusedLabelColor = Rose,
            cursorColor = if (isRobolectric) Color.Transparent else Rose,
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
