package com.example.fakestore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakestore.ui.screen.productdetail.ProductDetailScreen
import com.example.fakestore.ui.screen.productlist.ProductListScreen

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            ProductListScreen(onOpenDetail = { id -> nav.navigate(Routes.detail(id)) })
        }
        composable(
            route = Routes.DETAIL_ROUTE,
            arguments = listOf(navArgument(Routes.DETAIL_ARG_ID) { type = NavType.LongType })
        ) {
            ProductDetailScreen(onBack = { nav.popBackStack() })
        }
    }
}