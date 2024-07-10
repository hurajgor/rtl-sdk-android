package com.copart.rtlaisdk.ui.vehicleDetails

import android.net.Uri
import com.copart.rtlaisdk.data.model.VehicleMakesResponseBody
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponseBody
import com.copart.rtlaisdk.ui.base.ViewEvent
import com.copart.rtlaisdk.ui.base.ViewSideEffect
import com.copart.rtlaisdk.ui.base.ViewState

class VehicleDetailsContract {

    sealed class Event : ViewEvent {
        data object Retry : Event()
        data class OnVINChanged(val vin: String) : Event()
        data class OnGenerateRTLClicked(val vin: String) : Event()
        data class OnYearSelected(val year: String) : Event()
        data class OnMakeSelected(val make: String) : Event()
        data class OnModelSelected(val model: String) : Event()
        data class OnImageUrisChanged(val imageUri: Uri?, val index: Int) : Event()
    }

    data class State(
        val vinNumber: String,
        val year: String,
        val make: String,
        val model: String,
        val yearsList: List<VehicleYearsResponseBody>,
        val makesList: List<VehicleMakesResponseBody>,
        val modelsResponse: VehicleModelsResponse,
        val imageUris: List<Uri?>,
        val isLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data object ToRTLResults : Navigation()
        }
    }

}