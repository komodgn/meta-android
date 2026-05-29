package com.metasearch.android.feature.photo_detail.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.metasearch.android.feature.photo_detail.R

enum class PhotoDetailBottomBarItem(
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int,
    val description: String,
) {
    OPEN_AI(
        iconResId = R.drawable.ic_description,
        labelResId = R.string.photo_detail_screen_bottom_item_openai,
        description = "Create Image Description With Open AI",
    ),
    GRAPH(
        iconResId = R.drawable.ic_graph,
        labelResId = R.string.photo_detail_screen_bottom_item_graph,
        description = "Show Knowledge Graph",
    ),
    FOCUSING_SEARCH(
        iconResId = R.drawable.ic_search,
        labelResId = R.string.photo_detail_screen_bottom_item_focusing,
        description = "Go To Focusing Search Screen",
    ),
    SHARE(
        iconResId = R.drawable.ic_share,
        labelResId = R.string.photo_detail_screen_bottom_item_share,
        description = "Share Image",
    ),
}