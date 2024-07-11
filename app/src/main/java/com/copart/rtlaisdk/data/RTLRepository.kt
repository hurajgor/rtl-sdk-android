package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse
import com.copart.rtlaisdk.data.model.SellersListResponse
import com.copart.rtlaisdk.data.model.VINDecodeResponse
import com.copart.rtlaisdk.data.model.VehicleMakesResponse
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponse

interface RTLRepository {
    suspend fun getRtlList(): Result<RTLListResponse>
    suspend fun getVehicleYears(): Result<VehicleYearsResponse>
    suspend fun getVehicleMakes(vehicleType: String): Result<VehicleMakesResponse>
    suspend fun getVehicleModels(vehicleMake: String): Result<VehicleModelsResponse>
    suspend fun decodeVin(vin: String): Result<VINDecodeResponse>
    suspend fun getSellersList(): Result<SellersListResponse>
}