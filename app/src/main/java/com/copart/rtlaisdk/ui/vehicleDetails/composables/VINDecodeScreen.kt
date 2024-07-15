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
import com.copart.rtlaisdk.ui.common.Progress
import com.copart.rtlaisdk.ui.vehicleDetails.VehicleDetailsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun VINDecodeScreen(
    state: VehicleDetailsContract.State,
    effectFlow: Flow<VehicleDetailsContract.Effect>?,
    onEventSent: (event: VehicleDetailsContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: VehicleDetailsContract.Effect.Navigation) -> Unit,
    onUploadSuccessful: (requestId: String, isSuccess: Boolean) -> Unit
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

                is VehicleDetailsContract.Effect.UploadSuccessful -> {
                    onUploadSuccessful(effect.requestId, effect.isSuccess)
                }
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        it
        when {
            state.isError -> NetworkError { onEventSent(VehicleDetailsContract.Event.Retry) }
            state.isLoading -> Progress()
            else -> VINDecode(
                yearList = state.yearsList,
                makeList = state.makesList,
                sellersList = state.sellersList,
                primaryDamages = state.primaryDamages,
                modelsResponse = state.modelsResponse,
                imageUris = state.imageUris,
                onVinChanged = { vin -> onEventSent(VehicleDetailsContract.Event.OnVINChanged(vin)) },
                onModelSelected = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnModelSelected(
                            key, value
                        )
                    )
                },
                onMakeSelected = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnMakeSelected(
                            key, value
                        )
                    )
                },
                onYearSelected = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnYearSelected(
                            key, value
                        )
                    )
                },
                onGenerateRTL = { context ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnGenerateRTLClicked(
                            context
                        )
                    )
                },
                onImageUrisChanged = { imageUri, index ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnImageUrisChanged(
                            imageUri,
                            index
                        )
                    )
                },
                onSellerSelected = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnSellerSelected(
                            key, value
                        )
                    )
                },
                onPrimaryDamageSelected = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.OnPrimaryDamageSelected(
                            key, value
                        )
                    )
                },
                isAirBagsDeployed = { key, value ->
                    onEventSent(
                        VehicleDetailsContract.Event.IsAirBagsDeployed(
                            key, value
                        )
                    )
                },
            )
        }
    }

}