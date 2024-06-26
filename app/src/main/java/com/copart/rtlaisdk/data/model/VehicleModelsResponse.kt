package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class VehicleModelsResponse(
    @SerializedName("body") var body: VehicleModelsResponseBody? = VehicleModelsResponseBody()
)

data class VehicleModelsResponseBody(
    @SerializedName("list") var list: ArrayList<VehicleModelsList> = arrayListOf(),
    @SerializedName("moreDataAvailable") var moreDataAvailable: Boolean? = false
)

data class VehicleModelsList(
    @SerializedName("desc") var desc: String? = null
)