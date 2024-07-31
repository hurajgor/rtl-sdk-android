package com.copart.rtlaisdk.data.model

import com.google.gson.annotations.SerializedName

data class RTLDetailsResponse(
    @SerializedName("body") var body: RTLDetailsResponseBody? = RTLDetailsResponseBody()
)

data class RTLDetailsResponseBody(

    @SerializedName("images") var images: ArrayList<RTLDetailsResponseImages> = arrayListOf(),
    @SerializedName("vin") var vin: String = "",
    @SerializedName("claimNumber") var claimNumber: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("fuelType") var fuelType: String = "",
    @SerializedName("location") var location: String = "",
    @SerializedName("threshold") var threshold: String = "",
    @SerializedName("isEOTL") var isEOTL: Boolean = false,
    @SerializedName("confidenceLabel") var confidenceLabel: String = "",
    @SerializedName("finalDamageScore") var finalDamageScore: Double = 0.0,
    @SerializedName("finalNonDamageScore") var finalNonDamageScore: Double = 0.0,
    @SerializedName("flippedConfidenceLabel") var flippedConfidenceLabel: String = "",
    @SerializedName("flippedFinalDamageScore") var flippedFinalDamageScore: Double = 0.0,
    @SerializedName("flippedFinalNonDamageScore") var flippedFinalNonDamageScore: Double = 0.0,
    @SerializedName("totalLossReviewStatus") var totalLossReviewStatus: String = "",
    @SerializedName("totalLossReviewDesc") var totalLossReviewDesc: String = ""

)

data class RTLDetailsResponseImages(

    @SerializedName("imageURL") var imageURL: String = "",
    @SerializedName("createdDate") var createdDate: String = "",
    @SerializedName("imageName") var imageName: String = ""

)