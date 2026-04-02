package com.metasearch.android.core.permissions.api.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.metasearch.android.core.permissions.api.R
import com.metasearch.android.core.ui.component.MetaSearchDialog

@Composable
internal fun PermissionDialog(state: PermissionsState) {
    MetaSearchDialog(
        title = stringResource(R.string.permission_dialog_title),
        content = {
            Text(
                text = stringResource(R.string.permission_dialog_content),
            )
        },
        onConfirmRequest = {
            state.showRationale.value = false
            state.onGoToSettings()
        },
        confirmButtonText = stringResource(R.string.confirm_settings),
        onDismissRequest = { state.showRationale.value = false },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
    )
}
