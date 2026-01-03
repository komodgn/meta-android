package com.example.metasearch.core.common.extensions

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.clickableIfNotNull(onClick: (() -> Unit)?): Modifier =
    if (onClick != null) this.clickable(onClick = onClick) else this
