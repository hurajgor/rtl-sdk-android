package com.copart.rtlaisdk.ui.vehicleDetails

import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.ui.base.BaseViewModel

class VehicleDetailsViewModel(private val rtlRepository: RTLRepository) :
    BaseViewModel<VehicleDetailsContract.Event, VehicleDetailsContract.State, VehicleDetailsContract.Effect>() {

    override fun setInitialState() = VehicleDetailsContract.State(
        vinNumber = "",
        claimNumber = "",
        isError = false,
    )

    override fun handleEvents(event: VehicleDetailsContract.Event) {
        when (event) {
            is VehicleDetailsContract.Event.Retry -> {

            }

            is VehicleDetailsContract.Event.Decode -> {
                setEffect { VehicleDetailsContract.Effect.Navigation.ToVINDecodeResults(event.vin) }
            }
        }
    }
}