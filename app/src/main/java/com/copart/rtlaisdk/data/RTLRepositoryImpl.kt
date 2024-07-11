package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse
import com.copart.rtlaisdk.data.model.SellersListResponse
import com.copart.rtlaisdk.data.model.VINDecodeResponse
import com.copart.rtlaisdk.data.model.VehicleMakesResponse
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RTLRepositoryImpl(
    private val rtlApi: RTLApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RTLRepository {

    override suspend fun getRtlList(): Result<RTLListResponse> = makeApiCall(dispatcher) {
        rtlApi.getRTLList()
    }

    override suspend fun getVehicleYears(): Result<VehicleYearsResponse> = makeApiCall(dispatcher) {
        rtlApi.getVehicleYears()
    }

    override suspend fun getVehicleMakes(vehicleType: String): Result<VehicleMakesResponse> =
        makeApiCall(dispatcher) {
            rtlApi.getVehicleMakes(vehicleType)
        }

    override suspend fun getVehicleModels(vehicleMake: String): Result<VehicleModelsResponse> =
        makeApiCall(dispatcher) {
            rtlApi.getVehicleModels(vehicleMake)
        }

    override suspend fun decodeVin(vin: String): Result<VINDecodeResponse> =
        makeApiCall(dispatcher) {
            rtlApi.decodeVin(vin)
        }

    override suspend fun getSellersList(): Result<SellersListResponse> =
        makeApiCall(dispatcher) {
            rtlApi.getSellersList()
        }
}

suspend fun <T> makeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
): Result<T> = runCatching {
    withContext(dispatcher) {
        call.invoke()
    }
}