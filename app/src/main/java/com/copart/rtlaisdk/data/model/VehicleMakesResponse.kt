package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class VehicleMakesResponse(
    @SerializedName("body") var body: ArrayList<VehicleMakesResponseBody> = arrayListOf()
)

data class VehicleMakesResponseBody(
    @SerializedName("code") var code: String? = null,
    @SerializedName("desc") var desc: String? = null
)