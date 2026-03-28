package com.metasearch.android.core.testing.compose

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.metasearch.android.core.designsystem.theme.MetaSearchTheme

@Composable
fun MetaSearchTestContainer(
    content: @Composable () -> Unit,
) {
    MetaSearchTheme {
        Surface {
            CompositionLocalProvider(
                LocalLifecycleOwner provides FakeLocalLifecycleOwner(),
            ) {
                content()
            }
        }
    }
}

private class FakeLocalLifecycleOwner : LifecycleOwner {
    override val lifecycle: Lifecycle = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
}
