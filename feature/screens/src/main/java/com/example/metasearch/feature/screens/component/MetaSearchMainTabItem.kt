package com.example.metasearch.feature.screens.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.metasearch.feature.screens.GraphScreen
import com.example.metasearch.feature.screens.HomeScreen
import com.example.metasearch.feature.screens.PersonScreen
import com.slack.circuit.runtime.screen.Screen
import com.example.metasearch.feature.screens.R
import com.example.metasearch.feature.screens.NLSearchScreen

enum class MetaSearchMainTabItem(
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int,
    val screen: Screen,
) {
    PERSON(
        iconResId = R.drawable.icon_person,
        labelResId = R.string.person_label,
        screen = PersonScreen,
    ),
    HOME(
        iconResId = R.drawable.icon_home,
        labelResId = R.string.home_label,
        screen = HomeScreen,
    ),
    SEARCH(
        iconResId = R.drawable.icon_search,
        labelResId = R.string.search_label,
        screen = NLSearchScreen,
    ),
    GRAPH(
        iconResId = R.drawable.icon_graph,
        labelResId = R.string.graph_label,
        screen = GraphScreen,
    ),
}
