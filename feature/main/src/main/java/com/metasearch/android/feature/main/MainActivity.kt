package com.metasearch.android.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchDialogSpec
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.feature.screens.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dagger.hilt.android.AndroidEntryPoint
import tech.thdev.compose.exteions.system.ui.controller.rememberSystemUiController
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            LaunchedEffect(Unit) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = true,
                )
            }

            MetaSearchTheme {
                val dialogSpec = remember { mutableStateOf<MetaSearchDialogSpec?>(null) }

                val backStack = rememberSaveableBackStack(SplashScreen)
                val navigator = rememberCircuitNavigator(backStack)

                LaunchedEffect(Unit) {
                    EventHandler.eventFlow.collect { event ->
                        when (event) {
                            is MetaSearchEvent.ShowDialog -> dialogSpec.value = event.dialogSpec
                        }
                    }
                }

                dialogSpec.value?.let { spec ->
                    MetaSearchDialog(
                        onDismissRequest = {
                            dialogSpec.value = null
                        },
                        onConfirmRequest = {
                            spec.onConfirm()
                            dialogSpec.value = null
                        },
                        dismissButtonText = spec.dismissText,
                        confirmButtonText = spec.confirmText,
                        title = spec.title,
                    )
                }

                CircuitCompositionLocals(circuit) {
                    NavigableCircuitContent(
                        modifier = Modifier.fillMaxSize(),
                        backStack = backStack,
                        navigator = navigator,
                    )
                }
            }
        }
    }
}
