package com.copart.rtlaisdk.ui.vehicleDetails.composables

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.ui.base.SIDE_EFFECTS_KEY
import com.copart.rtlaisdk.ui.common.NetworkError
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun VINDecodeScreen(
    state: VehicleDetailsContract.State,
    effectFlow: Flow<VehicleDetailsContract.Effect>?,
    onEventSent: (event: VehicleDetailsContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: VehicleDetailsContract.Effect.Navigation) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(R.string.data_is_loaded)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is VehicleDetailsContract.Effect.DataWasLoaded -> {
                    snackBarHostState.showSnackbar(
                        message = snackBarMessage,
                        duration = SnackbarDuration.Short
                    )
                }

                is VehicleDetailsContract.Effect.Navigation.ToRTLResults -> onNavigationRequested(
                    effect
                )
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        it
        when {
            state.isError -> NetworkError { onEventSent(VehicleDetailsContract.Event.Retry) }
            else -> VINDecode(
                yearList = state.yearsList,
                makeList = state.makesList,
                modelsResponse = state.modelsResponse,
                onVinChanged = { vin -> onEventSent(VehicleDetailsContract.Event.OnVINChanged(vin)) },
                onModelSelected = { model ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnModelSelected(
                            model
                        )
                    )
                },
                onMakeSelected = { make ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnMakeSelected(
                            make
                        )
                    )
                },
                onYearSelected = { year ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnYearSelected(
                            year
                        )
                    )
                },
                onGenerateRTL = {
                    onEventSent(
                        VehicleDetailsContract.Event.OnGenerateRTLClicked(
                            state.vinNumber
                        )
                    )
                }
            )
        }
    }

}