package com.metasearch.android.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.designsystem.theme.Neutral500
import com.metasearch.android.core.designsystem.theme.White
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.feature.screens.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@OptIn(ExperimentalPermissionsApi::class)
@CircuitInject(SplashScreen::class, AppScope::class)
@Composable
fun SplashUi(
    modifier: Modifier = Modifier,
    state: SplashUiState,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = state.permissions)

    SplashSideEffectHandler(
        state = state,
        permissionState = permissionState,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.width(200.dp),
            contentDescription = "MetaSearch App Logo",
            painter = painterResource(com.metasearch.android.core.designsystem.R.drawable.ic_launcher_foreground),
        )

        if (state.showRationaleDialog) {
            MetaSearchDialog(
                title = stringResource(R.string.permission_dialog_title),
                content = {
                    Text(
                        text = stringResource(R.string.permission_dialog_content),
                        color = Neutral500,
                        style = MetaSearchTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                    )
                },
                onConfirmRequest = {
                    state.eventSink(SplashUiEvent.OnConfirmSettings)
                },
                confirmButtonText = stringResource(R.string.confirm_settings),
            )
        }
    }
}

@DevicePreview
@Composable
private fun SplashUiPreview() {
    MetaSearchTheme {
        SplashUi(
            state = SplashUiState(
                eventSink = {},
                showRationaleDialog = true,
            ),
        )
    }
}
