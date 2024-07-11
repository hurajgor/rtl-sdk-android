package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class UploadRTLResponse(
    @SerializedName("body") var body: UploadRTLBody? = UploadRTLBody()
)

data class UploadRTLBody(
    @SerializedName("code") var code: String = "",
    @SerializedName("uploadType") var uploadType: String = "",
    @SerializedName("entityType") var entityType: String = "",
    @SerializedName("requestId") var requestId: String = "",
    @SerializedName("originalFileUris") var originalFileUris: ArrayList<String> = arrayListOf()
)