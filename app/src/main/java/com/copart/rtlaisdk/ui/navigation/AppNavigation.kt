package com.copart.rtlaisdk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(onUploadSuccessful: (String, Boolean) -> Unit) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.Routes.VIN_DECODE
    ) {
        composable(
            route = Navigation.Routes.RTL_LIST
        ) {
            RTLListScreenDestination(navController)
        }

        composable(
            route = Navigation.Routes.VIN_DECODE
        ) {
            VINDecodeScreenDestination(navController) { requestId, isSuccess ->
                onUploadSuccessful(
                    requestId,
                    isSuccess
                )
            }
        }

    }
}

object Navigation {

    object Args {

    }

    object Routes {
        const val RTL_LIST = "rtlList"
        const val VIN_DECODE = "vinDecode"
    }

}

fun NavController.navigateToVinDecode() {
    navigate(route = Navigation.Routes.VIN_DECODE)
}
