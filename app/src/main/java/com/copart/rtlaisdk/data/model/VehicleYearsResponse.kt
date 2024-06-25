package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class VehicleYearsResponse(
    @SerializedName("body") var body: ArrayList<VehicleYearsResponseBody> = arrayListOf()
)

data class VehicleYearsResponseBody(
    @SerializedName("code") var code: Int? = null
)