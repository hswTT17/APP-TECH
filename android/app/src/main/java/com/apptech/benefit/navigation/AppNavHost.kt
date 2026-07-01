package com.apptech.benefit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptech.benefit.ui.detail.AppDetailScreen
import com.apptech.benefit.ui.list.AppListScreen

private const val ROUTE_LIST = "list"
private const val ROUTE_DETAIL = "detail/{appId}"

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ROUTE_LIST) {
        composable(ROUTE_LIST) {
            AppListScreen(
                onAppClick = { appId -> navController.navigate("detail/$appId") },
            )
        }
        composable(ROUTE_DETAIL) { backStackEntry ->
            val appId = backStackEntry.arguments?.getString("appId").orEmpty()
            AppDetailScreen(
                appId = appId,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
