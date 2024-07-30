package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.GetRTLListRequest
import com.copart.rtlaisdk.data.model.PrimaryDamagesResponse
import com.copart.rtlaisdk.data.model.RTLDetailsResponse
import com.copart.rtlaisdk.data.model.RTLListResponse
import com.copart.rtlaisdk.data.model.SellersListResponse
import com.copart.rtlaisdk.data.model.UploadRTLResponse
import com.copart.rtlaisdk.data.model.VINDecodeResponse
import com.copart.rtlaisdk.data.model.VehicleMakesResponse
import com.copart.rtlaisdk.data.model.VehicleModelsResponse
import com.copart.rtlaisdk.data.model.VehicleYearsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RTLApi {
    @POST(Endpoints.GET_RTL_LIST)
    suspend fun getRTLList(@Body request: GetRTLListRequest): RTLListResponse

    @GET(Endpoints.GET_VEHICLE_YEARS)
    suspend fun getVehicleYears(): VehicleYearsResponse

    @GET(Endpoints.GET_VEHICLE_MAKES)
    suspend fun getVehicleMakes(@Path("vehicleType") vehicleType: String): VehicleMakesResponse

    @GET(Endpoints.GET_VEHICLE_MODELS)
    suspend fun getVehicleModels(@Path("vehicleMake") vehicleMake: String): VehicleModelsResponse

    @GET(Endpoints.VIN_DECODE)
    suspend fun decodeVin(@Path("vin") vin: String): VINDecodeResponse

    @GET(Endpoints.SELLERS_LIST)
    suspend fun getSellersList(): SellersListResponse

    @GET(Endpoints.PRIMARY_DAMAGES_LIST)
    suspend fun getPrimaryDamages(): PrimaryDamagesResponse

    @Multipart
    @POST(Endpoints.UPLOAD_RTL)
    suspend fun uploadRTL(
        @Part("metadata") metadata: RequestBody,
        @Part imageFP: MultipartBody.Part,
        @Part imageFD: MultipartBody.Part,
        @Part imageRD: MultipartBody.Part,
        @Part imageRP: MultipartBody.Part
    ): UploadRTLResponse

    @GET(Endpoints.GET_RTL_ITEM)
    suspend fun getRTLDetails(@Path("requestId") requestId: String): RTLDetailsResponse
}