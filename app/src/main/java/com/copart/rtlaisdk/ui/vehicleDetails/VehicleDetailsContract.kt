package com.copart.rtlaisdk.ui.vehicleDetails

import com.copart.rtlaisdk.data.model.VehicleMakesResponseBody
import com.copart.rtlaisdk.data.model.VehicleYearsResponseBody
import com.copart.rtlaisdk.ui.base.ViewEvent
import com.copart.rtlaisdk.ui.base.ViewSideEffect
import com.copart.rtlaisdk.ui.base.ViewState

class VehicleDetailsContract {

    sealed class Event : ViewEvent {
        data object Retry : Event()
        data class Decode(val vin: String) : Event()
    }

    data class State(
        val vehicleYears: List<VehicleYearsResponseBody>,
        val vehicleMakes: List<VehicleMakesResponseBody>,
        val vinNumber: String,
        val claimNumber: String,
        val isVehicleYearsLoading: Boolean,
        val isVehicleMakesLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToVINDecodeResults(val requestId: String) : Navigation()
        }
    }

}