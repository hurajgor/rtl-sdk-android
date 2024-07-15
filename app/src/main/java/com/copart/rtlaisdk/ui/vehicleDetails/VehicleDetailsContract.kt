package com.copart.rtlaisdk.ui.vehicleDetails

import android.content.Context
import android.net.Uri
import com.copart.rtlaisdk.data.model.PrimaryDamagesItem
import com.copart.rtlaisdk.data.model.SellersListItem
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
        data class OnGenerateRTLClicked(val context: Context) : Event()
        data class OnYearSelected(val key: String, val value: String) : Event()
        data class OnMakeSelected(val key: String, val value: String) : Event()
        data class OnModelSelected(val key: String, val value: String) : Event()
        data class OnImageUrisChanged(val imageUri: Uri?, val index: Int) : Event()
        data class OnSellerSelected(val key: String, val value: String) : Event()
        data class OnPrimaryDamageSelected(val key: String, val value: String) : Event()
        data class IsAirBagsDeployed(val key: String, val value: String) : Event()
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
        val sellersList: List<SellersListItem>,
        val selectedSeller: SellersListItem?,
        val primaryDamages: List<PrimaryDamagesItem>,
        val selectedPrimaryDamage: PrimaryDamagesItem?,
        val isAirBagsDeployed: String,
        val isLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data object DataWasLoaded : Effect()
        data class UploadSuccessful(val requestId: String, val isSuccess: Boolean) : Effect()

        sealed class Navigation : Effect() {
            data object ToRTLResults : Navigation()
        }
    }

}