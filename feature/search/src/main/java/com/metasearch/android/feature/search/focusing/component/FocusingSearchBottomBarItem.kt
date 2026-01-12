package com.metasearch.android.feature.search.focusing.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.metasearch.android.feature.search.R

enum class FocusingSearchBottomBarItem(
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int,
    val description: String,
) {
    SEARCH(
        iconResId = R.drawable.ic_search_full,
        labelResId = R.string.focusing_search_button_text,
        description = "Search Icon",
    ),
    COLOR(
        iconResId = R.drawable.ic_pen,
        labelResId = R.string.focusing_search_change_circle_color_button,
        description = "Pen Icon",
    ),
    RESET(
        iconResId = R.drawable.ic_reset,
        labelResId = R.string.focusing_search_circle_reset_button,
        description = "Reset Icon",
    ),
}
