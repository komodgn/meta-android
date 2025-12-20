package com.example.metasearch.feature.splash

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
import com.example.metasearch.core.designsystem.annotation.DevicePreview
import com.example.metasearch.core.designsystem.theme.MetaSearchTheme
import com.example.metasearch.core.designsystem.theme.Neutral500
import com.example.metasearch.core.designsystem.theme.White
import com.example.metasearch.core.ui.component.MetaSearchDialog
import com.example.metasearch.feature.screens.SplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.android.components.ActivityRetainedComponent

@OptIn(ExperimentalPermissionsApi::class)
@CircuitInject(SplashScreen::class, ActivityRetainedComponent::class)
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
            painter = painterResource(com.example.metasearch.core.designsystem.R.drawable.ic_launcher_foreground),
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
                onDismissRequest = {
                    state.eventSink(SplashUiEvent.OnConfirmSettings)
                },
                dismissButtonText = stringResource(R.string.confirm_settings),
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
            )
        )
    }
}
