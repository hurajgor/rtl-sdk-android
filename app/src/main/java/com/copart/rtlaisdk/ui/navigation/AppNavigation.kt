package com.copart.rtlaisdk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.RTL_LIST
    ) {
        composable(
            route = Navigation.Routes.RTL_LIST
        ) {
            RTLListScreenDestination(navController)
        }

    }
}

object Navigation {

    object Args {

    }

    object Routes {
        const val RTL_LIST = "rtlList"
    }

}
