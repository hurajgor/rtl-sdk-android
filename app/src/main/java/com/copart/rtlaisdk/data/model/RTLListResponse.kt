package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class RTLListResponse(
    @SerializedName("body") var body: Body? = Body()
)

data class Body(
    @SerializedName("response") var response: Response? = Response(),
//    @SerializedName("facet_counts") var facetCounts: FacetCounts? = FacetCounts()
)

data class Response(
    @SerializedName("numFound") var numFound: Int? = null,
    @SerializedName("start") var start: Int? = null,
    @SerializedName("numFoundExact") var numFoundExact: Boolean? = null,
    @SerializedName("docs") var docs: ArrayList<RTLListItem> = arrayListOf()
)

data class RTLListItem(
    @SerializedName("vehicleType") var vehicleType: String? = "",
    @SerializedName("humanReviewRequired") var humanReviewRequired: Boolean? = null,
    @SerializedName("status") var status: String? = "",
    @SerializedName("requestId") var requestId: Int? = null,
    @SerializedName("year") var year: String? = "",
    @SerializedName("make") var make: String? = "",
    @SerializedName("model") var model: String? = "",
    @SerializedName("sellerCode") var sellerCode: String? = "",
    @SerializedName("sellerCompany") var sellerCompany: String? = "",
    @SerializedName("vinNumber") var vinNumber: String? = "",
    @SerializedName("created") var created: String? = "",
    @SerializedName("sellerId") var sellerId: String? = "",
    @SerializedName("imageFP") var imageFP: String? = "",
    @SerializedName("imageFD") var imageFD: String? = "",
    @SerializedName("imageRD") var imageRD: String? = "",
    @SerializedName("imageRP") var imageRP: String? = "",
    @SerializedName("sellerCompanyId") var sellerCompanyId: String? = "",
    @SerializedName("claimNumber") var claimNumber: String? = ""
)

data class FacetCounts(
    @SerializedName("facet_fields") var facetFields: FacetFields? = FacetFields(),
)

data class FacetFields(
    @SerializedName("seller_company") var sellerCompany: ArrayList<String> = arrayListOf(),
    @SerializedName("seller_code") var sellerCode: ArrayList<String> = arrayListOf(),
    @SerializedName("total_loss_requested_status") var totalLossRequestedStatus: ArrayList<String> = arrayListOf(),
    @SerializedName("vehicle_type") var vehicleType: String? = null,
    @SerializedName("lynk_user_full_name") var lynkUserFullName: ArrayList<String> = arrayListOf(),
    @SerializedName("seller_name") var sellerName: ArrayList<String> = arrayListOf()
)

fun buildRTLListItemPreview() = RTLListItem(
    vehicleType = "AUTOMOBILE",
    humanReviewRequired = true,
    status = "Auto Completed",
    requestId = 128663,
    year = "2001",
    make = "JEEP",
    model = "WRANGLER /",
    sellerCode = "*CDS",
    sellerCompany = "COPART DEALER SERVICES",
    vinNumber = "1J4FA49S61P361516",
    created = "2024-06-18T16:12:58Z",
    sellerId = "SSM276580",
    imageFD = "https://c-static-qa.copart.com/v1/AUTH_svc.qdoc00001/ids-c-qa-slrpubperm/0524/77efa9a9f46a44b4917b1f6a67a188f0_O_1.jpg",
    imageFP = "https://c-static-qa.copart.com/v1/AUTH_svc.qdoc00001/ids-c-qa-slrpubperm/0524/77efa9a9f46a44b4917b1f6a67a188f0_O_0.jpg",
    imageRD = "https://c-static-qa.copart.com/v1/AUTH_svc.qdoc00001/ids-c-qa-slrpubperm/0524/77efa9a9f46a44b4917b1f6a67a188f0_O_2.jpg",
    imageRP = "https://c-static-qa.copart.com/v1/AUTH_svc.qdoc00001/ids-c-qa-slrpubperm/0524/77efa9a9f46a44b4917b1f6a67a188f0_O_3.jpg",
    sellerCompanyId = "",
    claimNumber = ""
)
