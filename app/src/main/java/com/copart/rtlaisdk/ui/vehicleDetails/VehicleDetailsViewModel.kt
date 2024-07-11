package com.copart.rtlaisdk.ui.vehicleDetails

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
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
        getSellersList()
        getPrimaryDamages()
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

    private fun getSellersList() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getSellersList()
                .onSuccess { response ->
                    setState {
                        copy(
                            sellersList = response.body?.sellersList ?: emptyList(),
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

    private fun getPrimaryDamages() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getPrimaryDamages()
                .onSuccess { response ->
                    setState {
                        copy(
                            primaryDamages = response.body,
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
        imageUris = mutableStateListOf<Uri?>(null, null, null, null),
        sellersList = arrayListOf(),
        selectedSeller = null,
        primaryDamages = emptyList(),
        selectedPrimaryDamage = null,
        isAirBagsDeployed = "No",
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
                        make = event.value,
                        model = "",
                        modelsResponse = VehicleModelsResponse()
                    )
                }
                getVehicleModel(event.key)
            }

            is VehicleDetailsContract.Event.OnModelSelected -> setState { copy(model = event.value) }
            is VehicleDetailsContract.Event.OnYearSelected -> setState { copy(year = event.value) }
            is VehicleDetailsContract.Event.OnImageUrisChanged -> {
                val imageUris = viewState.value.imageUris.toMutableList()
                imageUris[event.index] = event.imageUri
                setState { copy(imageUris = imageUris) }
            }

            is VehicleDetailsContract.Event.OnSellerSelected -> {
                val selectedSeller = viewState.value.sellersList.find { it.id == event.key }
                setState { copy(selectedSeller = selectedSeller) }
            }

            is VehicleDetailsContract.Event.OnPrimaryDamageSelected -> {
                val selectedPrimaryDamage =
                    viewState.value.primaryDamages.find { it.code == event.key }
                setState { copy(selectedPrimaryDamage = selectedPrimaryDamage) }
            }

            is VehicleDetailsContract.Event.IsAirBagsDeployed -> {
                setState { copy(isAirBagsDeployed = event.value) }
            }
        }
    }
}