package com.copart.rtlaisdk.ui.vehicleDetails

import com.copart.rtlaisdk.ui.base.ViewEvent
import com.copart.rtlaisdk.ui.base.ViewSideEffect
import com.copart.rtlaisdk.ui.base.ViewState

class VehicleDetailsContract {

    sealed class Event : ViewEvent {
        data object Retry : Event()
        data class Decode(val vin: String) : Event()
    }

    data class State(
        val vinNumber: String,
        val claimNumber: String,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToVINDecodeResults(val requestId: String) : Navigation()
        }
    }

}