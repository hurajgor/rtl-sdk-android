package com.copart.rtlaisdk.ui.vehicleDetails

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.copart.rtlaisdk.RTLAIApplication
import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.data.model.RTLUploadMetadata
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.ui.base.BaseViewModel
import com.copart.rtlaisdk.utils.VEHICLE_TYPE_CODE_MAPPING
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
            setState { copy(isLoading = false, isError = false) }

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
            setState { copy(isLoading = false, isError = false) }

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
            setState { copy(isLoading = false, isError = false) }

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
            setState { copy(isLoading = false, isError = false) }

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
            setState { copy(isLoading = false, isError = false) }

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

    private fun createPartFromString(value: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), value)
    }

    private fun prepareFilePart(
        context: Context,
        partName: String,
        fileUri: Uri?
    ): MultipartBody.Part {
        val inputStream = fileUri?.let { context.contentResolver.openInputStream(it) }
        val requestFile = inputStream?.readBytes()?.let {
            RequestBody.create(
                context.contentResolver.getType(fileUri)?.toMediaTypeOrNull(), it
            )
        }
        return MultipartBody.Part.createFormData(
            partName,
            "${partName}.jpg", requestFile!!
        )
    }

    private fun uploadRTL(context: Context, metadata: String, imageUris: List<Uri?>) {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }
            val metadataPart = createPartFromString(metadata)

            val imageFPPart = prepareFilePart(context, "imageFP", imageUris[0])
            val imageFDPart = prepareFilePart(context, "imageFD", imageUris[1])
            val imageRDPart = prepareFilePart(context, "imageRD", imageUris[2])
            val imageRPPart = prepareFilePart(context, "imageRP", imageUris[3])

            rtlRepository.uploadRTL(
                metadataPart,
                imageFPPart,
                imageFDPart,
                imageRDPart,
                imageRPPart
            )
                .onSuccess { response ->
                    setState { copy(isLoading = false, isError = false) }
                    setEffect {
                        VehicleDetailsContract.Effect.UploadSuccessful(
                            response.body?.requestId ?: "", true
                        )
                    }
                }
                .onFailure { error ->
                    println(error)
                    setState { copy(isError = false, isLoading = false) }
                    setEffect { VehicleDetailsContract.Effect.UploadSuccessful("", false) }
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
            is VehicleDetailsContract.Event.OnGenerateRTLClicked -> {
                val rtlClientParams = RTLAIApplication.rtlClientParams
                val metaData = RTLUploadMetadata(
                    seller_id = rtlClientParams?.sellerId ?: "",
                    vin_number = viewState.value.vinNumber,
                    user_id = rtlClientParams?.sellerId ?: "",
                    seller_code = viewState.value.selectedSeller?.id ?: "",
                    seller_email = rtlClientParams?.sellerEmail ?: "",
                    operating_country_grp_code = rtlClientParams?.operatingCountryGroupCode ?: "",
                    appSource = rtlClientParams?.appSource ?: "",
                    acv = "1.00",
                    make = viewState.value.make,
                    model = viewState.value.model,
                    year = viewState.value.year,
                    vehicle_type = "AUTOMOBILE",
                    airBags = viewState.value.isAirBagsDeployed,
                    damage_type = viewState.value.selectedPrimaryDamage?.code ?: "",
                    odometer = 0,
                    location = "",
                    fuel_type = "Gas",
                    lot_cat = "A",
                    group_model = viewState.value.model,
                    country_code = rtlClientParams?.countryCode ?: "",
                    claim_number = null,
                    re_request = false
                )
                uploadRTL(event.context, Gson().toJson(metaData), viewState.value.imageUris)
            }

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

            VehicleDetailsContract.Event.RedirectToRTLLists -> setEffect {
                VehicleDetailsContract.Effect.Navigation.ToRTLLists
            }

            VehicleDetailsContract.Event.OnValidationFailed -> setEffect { VehicleDetailsContract.Effect.ValidationFailed }
        }
    }
}