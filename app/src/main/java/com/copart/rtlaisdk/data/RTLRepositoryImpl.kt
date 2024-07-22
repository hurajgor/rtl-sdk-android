package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.GetRTLListRequest
import com.copart.rtlaisdk.data.model.PrimaryDamagesResponse
import com.copart.rtlaisdk.data.model.RTLListResponse
import com.copart.rtlaisdk.data.model.SellersListResponse
import com.copart.rtlaisdk.data.model.UploadRTLResponse
import com.copart.rtlaisdk.data.model.VINDecodeResponse
import com.copart.rtlaisdk.data.model.VehicleMakesResponse
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RTLRepositoryImpl(
    private val rtlApi: RTLApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RTLRepository {

    override suspend fun getRtlList(request: GetRTLListRequest): Result<RTLListResponse> =
        makeApiCall(dispatcher) {
            rtlApi.getRTLList(request)
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

    override suspend fun getPrimaryDamages(): Result<PrimaryDamagesResponse> =
        makeApiCall(dispatcher) {
            rtlApi.getPrimaryDamages()
        }

    override suspend fun uploadRTL(
        metadata: RequestBody,
        imageFP: MultipartBody.Part,
        imageFD: MultipartBody.Part,
        imageRD: MultipartBody.Part,
        imageRP: MultipartBody.Part
    ): Result<UploadRTLResponse> =
        makeApiCall(dispatcher) {
            rtlApi.uploadRTL(metadata, imageFP, imageFD, imageRD, imageRP)
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