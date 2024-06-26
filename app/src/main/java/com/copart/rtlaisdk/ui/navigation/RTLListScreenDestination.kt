package com.copart.rtlaisdk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.copart.rtlaisdk.ui.rtlList.RTLListContract
import com.copart.rtlaisdk.ui.rtlList.RTLListViewModel
import com.copart.rtlaisdk.ui.rtlList.composables.RTLListScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun RTLListScreenDestination(navController: NavController) {
    val viewModel = getViewModel<RTLListViewModel>()
    RTLListScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            if (navigationEffect is RTLListContract.Effect.Navigation.ToRTLDetails) {
//                navController.navigateToRTLDetails(navigationEffect.requestId)
            }
            if (navigationEffect is RTLListContract.Effect.Navigation.ToVINDecode) {
                navController.navigateToVinDecode()
            }
        })
}
