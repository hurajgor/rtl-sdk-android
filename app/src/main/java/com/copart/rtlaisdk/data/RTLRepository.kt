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
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RTLRepository {
    suspend fun getRtlList(request: GetRTLListRequest): Result<RTLListResponse>
    suspend fun getVehicleYears(): Result<VehicleYearsResponse>
    suspend fun getVehicleMakes(vehicleType: String): Result<VehicleMakesResponse>
    suspend fun getVehicleModels(vehicleMake: String): Result<VehicleModelsResponse>
    suspend fun decodeVin(vin: String): Result<VINDecodeResponse>
    suspend fun getSellersList(): Result<SellersListResponse>
    suspend fun getPrimaryDamages(): Result<PrimaryDamagesResponse>
    suspend fun uploadRTL(
        metadata: RequestBody,
        imageFP: MultipartBody.Part,
        imageFD: MultipartBody.Part,
        imageRD: MultipartBody.Part,
        imageRP: MultipartBody.Part
    ): Result<UploadRTLResponse>

}