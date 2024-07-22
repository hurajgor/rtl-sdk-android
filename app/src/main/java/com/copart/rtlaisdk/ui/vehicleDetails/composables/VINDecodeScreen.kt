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
    val dataLoadedMessage = stringResource(R.string.data_is_loaded)
    val rtlRequestGeneratedMessage = stringResource(R.string.rtl_request_generated)
    val validationFailedMessage = stringResource(R.string.please_fill_all_the_fields)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is VehicleDetailsContract.Effect.DataWasLoaded -> {
                    /*snackBarHostState.showSnackbar(
                        message = dataLoadedMessage,
                        duration = SnackbarDuration.Short
                    )*/
                }

                is VehicleDetailsContract.Effect.Navigation.ToRTLResults -> onNavigationRequested(
                    effect
                )

                VehicleDetailsContract.Effect.RTLRequestGenerated -> {
                    snackBarHostState.showSnackbar(
                        message = rtlRequestGeneratedMessage,
                        duration = SnackbarDuration.Short
                    )
                    onEventSent(VehicleDetailsContract.Event.RedirectToRTLLists)
                }

                is VehicleDetailsContract.Effect.Navigation.ToRTLLists -> onNavigationRequested(
                    effect
                )

                is VehicleDetailsContract.Effect.ValidationFailed -> {
                    snackBarHostState.showSnackbar(
                        message = validationFailedMessage,
                        duration = SnackbarDuration.Short
                    )
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
                    if (state.vinNumber.isEmpty() || state.year.isEmpty() || state.make.isEmpty() || state.model.isEmpty() || state.selectedSeller == null || state.selectedPrimaryDamage == null || state.imageUris.contains(
                            null
                        )
                    ) {
                        onEventSent(VehicleDetailsContract.Event.OnValidationFailed)
                    } else {
                        onEventSent(
                            VehicleDetailsContract.Event.OnGenerateRTLClicked(
                                context
                            )
                        )
                    }
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