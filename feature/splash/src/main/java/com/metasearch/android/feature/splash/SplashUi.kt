package com.metasearch.android.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metasearch.android.core.designsystem.annotation.DevicePreview
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.permissions.api.ui.PermissionsState
import com.metasearch.android.feature.screens.SplashScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope

@CircuitInject(SplashScreen::class, AppScope::class)
@Composable
fun SplashUi(
    modifier: Modifier = Modifier,
    state: SplashUiState,
) {
    val permissionState = PermissionsState.rememberPermissionsState(
        permissions = state.permissions,
        onGoToSettings = { state.eventSink(SplashUiEvent.OnConfirmSettings) },
    )

    SplashSideEffectHandler(
        state = state,
        permissionState = permissionState,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MetaSearchTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.width(200.dp),
            contentDescription = "MetaSearch App Logo",
            painter = painterResource(com.metasearch.android.core.designsystem.R.drawable.ic_launcher_foreground),
        )
    }
}

@DevicePreview
@Composable
private fun SplashUiPreview() {
    MetaSearchTheme {
        SplashUi(
            state = SplashUiState.mock(),
        )
    }
}
