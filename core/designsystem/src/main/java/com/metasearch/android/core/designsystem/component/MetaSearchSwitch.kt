package com.metasearch.android.core.designsystem.component

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.metasearch.android.core.designsystem.annotation.ComponentPreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedTrackColor = MetaSearchTheme.colors.actionPrimary,
            checkedThumbColor = MetaSearchTheme.colors.actionContent,
            uncheckedTrackColor = MetaSearchTheme.colors.surfaceVariant,
            uncheckedThumbColor = MetaSearchTheme.colors.contentSecondary,
            uncheckedBorderColor = MetaSearchTheme.colors.outline,
        ),
    )
}

@ComponentPreview
@Composable
private fun MetaSearchSwitchCheckedPreview() {
    MetaSearchTheme {
        MetaSearchSwitch(
            checked = true,
            onCheckedChange = {},
        )
    }
}

@ComponentPreview
@Composable
private fun MetaSearchSwitchUncheckedPreview() {
    MetaSearchTheme {
        MetaSearchSwitch(
            checked = false,
            onCheckedChange = {},
        )
    }
}
