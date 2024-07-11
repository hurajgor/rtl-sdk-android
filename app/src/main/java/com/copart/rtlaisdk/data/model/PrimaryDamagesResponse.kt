package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class PrimaryDamagesResponse(
    @SerializedName("body") var body: ArrayList<PrimaryDamagesItem> = arrayListOf()
)

data class PrimaryDamagesItem(
    @SerializedName("code") var code: String = "",
    @SerializedName("desc") var desc: String = ""

)