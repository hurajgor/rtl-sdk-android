package com.copart.rtlaisdk.ui.vehicleDetails

import androidx.lifecycle.viewModelScope
import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.ui.base.BaseViewModel
import com.copart.rtlaisdk.utils.VEHICLE_TYPE_CODE_MAPPING
import kotlinx.coroutines.launch

class VehicleDetailsViewModel(private val rtlRepository: RTLRepository) :
    BaseViewModel<VehicleDetailsContract.Event, VehicleDetailsContract.State, VehicleDetailsContract.Effect>() {

    init {
        getVehicleYears()
        getVehicleMakes()
    }

    override fun setInitialState() = VehicleDetailsContract.State(
        vehicleYears = emptyList(),
        vehicleMakes = emptyList(),
        vinNumber = "",
        claimNumber = "",
        isVehicleYearsLoading = true,
        isVehicleMakesLoading = true,
        isError = false,
    )

    override fun handleEvents(event: VehicleDetailsContract.Event) {
        when (event) {
            is VehicleDetailsContract.Event.Retry -> {
                getVehicleYears()
                getVehicleMakes()
            }

            is VehicleDetailsContract.Event.Decode -> {
                setEffect { VehicleDetailsContract.Effect.Navigation.ToVINDecodeResults(event.vin) }
            }
        }
    }

    private fun getVehicleYears() {
        viewModelScope.launch {
            setState { copy(isVehicleYearsLoading = true, isError = false) }

            rtlRepository.getVehicleYears()
                .onSuccess { response ->
                    setState {
                        copy(
                            vehicleYears = response.body ?: emptyList(),
                            isVehicleYearsLoading = false
                        )
                    }
                    setEffect { VehicleDetailsContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isVehicleYearsLoading = false) }
                }
        }
    }

    private fun getVehicleMakes() {
        viewModelScope.launch {
            setState { copy(isVehicleMakesLoading = true, isError = false) }

            rtlRepository.getVehicleMakes(VEHICLE_TYPE_CODE_MAPPING.AUTOMOBILES)
                .onSuccess { response ->
                    setState {
                        copy(
                            vehicleMakes = response.body ?: emptyList(),
                            isVehicleMakesLoading = false
                        )
                    }
                    setEffect { VehicleDetailsContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isVehicleMakesLoading = false) }
                }
        }
    }

}