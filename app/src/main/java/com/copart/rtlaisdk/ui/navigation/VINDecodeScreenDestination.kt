package com.copart.rtlaisdk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsContract
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsViewModel
import com.copart.rtlaisdk.ui.vehicleDetails.composables.VINDecodeScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun VINDecodeScreenDestination(navController: NavController) {
    val viewModel = getViewModel<VehicleDetailsViewModel>()
    VINDecodeScreen(
        state = viewModel.viewState.value,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is VehicleDetailsContract.Effect.Navigation.ToRTLResults -> {
                    // TODO: Navigate to results screen
                }

                is VehicleDetailsContract.Effect.Navigation.ToRTLLists -> {
                    navController.navigateToRTLList()
                }
            }
        }
    )
}
