package com.copart.rtlaisdk.ui.vehicleDetails

import androidx.lifecycle.viewModelScope
import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.ui.base.BaseViewModel
import com.copart.rtlaisdk.utils.VEHICLE_TYPE_CODE_MAPPING
import kotlinx.coroutines.launch

class VehicleDetailsViewModel(private val rtlRepository: RTLRepository) :
    BaseViewModel<VehicleDetailsContract.Event, VehicleDetailsContract.State, VehicleDetailsContract.Effect>() {

    init {
        getVehicleYearsAndMakes()
    }

    private fun getVehicleYearsAndMakes() {
        getVehicleYears()
        getVehicleMakes()
    }

    private fun getVehicleMakes() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getVehicleMakes(VEHICLE_TYPE_CODE_MAPPING.AUTOMOBILES)
                .onSuccess { response ->
                    setState {
                        copy(
                            makesList = response.body,
                            isLoading = false
                        )
                    }
                    setEffect { VehicleDetailsContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isLoading = false) }
                }
        }
    }

    private fun getVehicleYears() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getVehicleYears()
                .onSuccess { response ->
                    setState {
                        copy(
                            yearsList = response.body,
                            isLoading = false
                        )
                    }
                    setEffect { VehicleDetailsContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isLoading = false) }
                }
        }
    }

    private fun getVehicleModel(make: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getVehicleModels(make)
                .onSuccess { response ->
                    setState {
                        copy(
                            modelsResponse = response,
                            isLoading = false
                        )
                    }
                    setEffect { VehicleDetailsContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isLoading = false) }
                }
        }
    }

    override fun setInitialState() = VehicleDetailsContract.State(
        vinNumber = "",
        year = "",
        make = "",
        model = "",
        yearsList = emptyList(),
        makesList = emptyList(),
        modelsResponse = VehicleModelsResponse(),
        isLoading = false,
        isError = false,
    )

    override fun handleEvents(event: VehicleDetailsContract.Event) {
        when (event) {
            is VehicleDetailsContract.Event.Retry -> getVehicleYearsAndMakes()
            is VehicleDetailsContract.Event.OnVINChanged -> setState { copy(vinNumber = event.vin) }
            is VehicleDetailsContract.Event.OnGenerateRTLClicked -> setEffect { VehicleDetailsContract.Effect.Navigation.ToRTLResults }
            is VehicleDetailsContract.Event.OnMakeSelected -> {
                setState {
                    copy(
                        make = event.make,
                        model = "",
                        modelsResponse = VehicleModelsResponse()
                    )
                }
                val makeCode = viewState.value.makesList.find { it.desc == event.make }?.code ?: ""
                getVehicleModel(makeCode)
            }

            is VehicleDetailsContract.Event.OnModelSelected -> setState { copy(model = event.model) }
            is VehicleDetailsContract.Event.OnYearSelected -> setState { copy(year = event.year) }
        }
    }
}