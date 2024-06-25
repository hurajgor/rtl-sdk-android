package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse
import com.copart.rtlaisdk.data.model.VINDecodeResponse
import com.copart.rtlaisdk.data.model.VehicleMakesResponse
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RTLApi {
    @POST(Endpoints.GET_RTL_LIST)
    suspend fun getRTLList(): RTLListResponse

    @GET(Endpoints.GET_VEHICLE_YEARS)
    suspend fun getVehicleYears(): VehicleYearsResponse

    @GET(Endpoints.GET_VEHICLE_MAKES)
    suspend fun getVehicleMakes(@Path("vehicleType") vehicleType: String): VehicleMakesResponse

    @GET(Endpoints.GET_VEHICLE_MODELS)
    suspend fun getVehicleModels(@Path("vehicleMake") vehicleMake: String): VehicleModelsResponse

    @GET(Endpoints.VIN_DECODE)
    suspend fun decodeVin(@Path("vin") vin: String): VINDecodeResponse
}