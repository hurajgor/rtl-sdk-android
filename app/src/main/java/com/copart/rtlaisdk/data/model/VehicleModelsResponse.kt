package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class VehicleModelsResponse(
    @SerializedName("body") var body: VehicleModelsResponseBody? = VehicleModelsResponseBody()
)

data class VehicleModelsResponseBody(
    @SerializedName("list") var list: ArrayList<List> = arrayListOf(),
    @SerializedName("moreDataAvailable") var moreDataAvailable: Boolean? = false
)

data class List(
    @SerializedName("desc") var desc: String? = null
)