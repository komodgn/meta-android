package com.metasearch.android.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SplashScreen : Screen

@Parcelize
data object HomeScreen : Screen

@Parcelize
data object GraphScreen : Screen

@Parcelize
data object PersonScreen : Screen

@Parcelize
data object NLSearchScreen : Screen

@Parcelize
data class PersonDetailScreen(
    val personId: Long,
) : Screen

@Parcelize
data class PhotoDetailScreen(
    val imageUriString: String,
) : Screen

@Parcelize
data class FocusingSearchScreen(
    val imageUriString: String,
) : Screen

@Parcelize
data class GraphDetailScreen(
    val imageUriString: String,
) : Screen

@Parcelize
data class WebViewScreen(
    val url: String,
) : Screen
