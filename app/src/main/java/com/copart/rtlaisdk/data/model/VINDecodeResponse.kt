package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class VINDecodeResponse(
    @SerializedName("body") var body: VINDecodeResponseBody? = VINDecodeResponseBody()
)

data class VINDecodeResponseBody(
    @SerializedName("makeDesc") var makeDesc: String? = null,
    @SerializedName("modelDesc") var modelDesc: String? = null,
    @SerializedName("vinYear") var vinYear: String? = null,
    @SerializedName("vinMake") var vinMake: String? = null,
    @SerializedName("vinModel") var vinModel: String? = null,
    @SerializedName("bodyStyle") var bodyStyle: String? = null,
    @SerializedName("engine") var engine: String? = null,
    @SerializedName("vinDrive") var vinDrive: String? = null,
    @SerializedName("xmission") var xmission: String? = null,
    @SerializedName("cylinder") var cylinder: String? = null,
    @SerializedName("fuel") var fuel: String? = null,
    @SerializedName("driveLongDesc") var driveLongDesc: String? = null,
    @SerializedName("nadaAcv") var nadaAcv: String? = null,
    @SerializedName("driveTrain") var driveTrain: String? = null
)