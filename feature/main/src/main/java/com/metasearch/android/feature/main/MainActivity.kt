package com.metasearch.android.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.metasearch.android.core.common.utils.EventHandler
import com.metasearch.android.core.common.utils.MetaSearchDialogSpec
import com.metasearch.android.core.common.utils.MetaSearchEvent
import com.metasearch.android.core.designsystem.component.MetaSearchToast
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme
import com.metasearch.android.core.ui.component.MetaSearchDialog
import com.metasearch.android.feature.main.deeplink.DeepLinkParser
import com.metasearch.android.feature.screens.SplashScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

        val intentData = intent?.data

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
                var toastMessage by remember { mutableStateOf<String?>(null) }

                val initialScreens = remember {
                    DeepLinkParser.parse(intentData) ?: listOf(SplashScreen)
                }

                val backStack = rememberSaveableBackStack(initialScreens)
                val navigator = rememberCircuitNavigator(backStack)

                LaunchedEffect(Unit) {
                    EventHandler.eventFlow.collect { event ->
                        when (event) {
                            is MetaSearchEvent.ShowDialog -> {
                                dialogSpec.value = event.dialogSpec
                            }

                            is MetaSearchEvent.ShowToast -> {
                                toastMessage = event.message
                            }
                        }
                    }
                }

                LaunchedEffect(toastMessage) {
                    if (toastMessage != null) {
                        delay(1500L)
                        toastMessage = null
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    dialogSpec.value?.let { spec ->
                        MetaSearchDialog(
                            onDismissRequest = {
                                dialogSpec.value = null
                            },
                            onConfirmRequest = {
                                spec.onConfirm()
                                dialogSpec.value = null
                            },
                            dismissButtonText = spec.dismissText?.asString(),
                            confirmButtonText = spec.confirmText.asString(),
                            title = spec.title,
                        )
                    }

                    CircuitCompositionLocals(circuit) {
                        NavigableCircuitContent(
                            modifier = Modifier.fillMaxSize(),
                            backStack = backStack,
                            navigator = navigator,
                            decoratorFactory = remember(navigator) {
                                GestureNavigationDecorationFactory(
                                    onBackInvoked = navigator::pop,
                                )
                            },
                        )
                    }

                    MetaSearchToast(
                        isVisible = toastMessage != null,
                        message = toastMessage ?: "",
                    )
                }
            }
        }
    }
}
