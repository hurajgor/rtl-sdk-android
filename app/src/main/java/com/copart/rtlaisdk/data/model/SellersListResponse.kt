package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class SellersListResponse(
    @SerializedName("body") var body: SellerListResponseBody? = SellerListResponseBody()
)

data class SellerListResponseBody(
    @SerializedName("list") var sellersList: ArrayList<SellersListItem> = arrayListOf(),
)

data class SellersListItem(
    @SerializedName("ss") var ss: String = "",
    @SerializedName("cn") var cn: String = "",
    @SerializedName("sc") var sc: String = "",
    @SerializedName("id") var id: String = "",
    @SerializedName("value") var value: String = "",
    @SerializedName("label") var label: String = "",
    @SerializedName("key") var key: String = ""
)