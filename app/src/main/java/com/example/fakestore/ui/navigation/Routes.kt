package com.example.fakestore.ui.navigation

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail"
    const val DETAIL_ARG_ID = "id"
    const val DETAIL_ROUTE = "$DETAIL/{$DETAIL_ARG_ID}"

    fun detail(id: Long) = "$DETAIL/$id"
}