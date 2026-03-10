package com.metasearch.android.feature.main.deeplink

import android.net.Uri
import com.metasearch.android.feature.screens.GraphDetailScreen
import com.metasearch.android.feature.screens.GraphScreen
import com.metasearch.android.feature.screens.HomeScreen
import com.metasearch.android.feature.screens.NLSearchScreen
import com.slack.circuit.runtime.screen.Screen

object DeepLinkParser {
    fun parse(uri: Uri?): List<Screen>? {
        if (uri == null) return null

        val screens = mutableListOf<Screen>()

        when (uri.host) {
            "search" -> screens.add(NLSearchScreen)
            "graph" -> {
                val isDetail = uri.pathSegments.contains("detail")
                val entityName = uri.getQueryParameter("entityName")

                if (isDetail && entityName != null) {
                    screens.add(HomeScreen)
                    screens.add(GraphDetailScreen(entityName))
                } else {
                    screens.add(GraphScreen)
                }
            }
        }

        return screens.takeIf { it.isNotEmpty() }
    }
}
