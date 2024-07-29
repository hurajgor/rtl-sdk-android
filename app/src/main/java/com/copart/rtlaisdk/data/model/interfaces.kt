package com.copart.rtlaisdk.data.model

import androidx.compose.ui.graphics.painter.Painter

data class ImagePlaceholder(
    val placeholder: Painter,
    val overlay: Int,
    val contentDescription: String
)

data class RTLUploadMetadata(
    val seller_id: String = "SSM33594",
    val vin_number: String? = null,
    val user_id: String = "SSM33594",
    val seller_code: String = "",
    val seller_email: String = "",
    val operating_country_grp_code: String = "",
    val appSource: String = "",
    val acv: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val vehicle_type: String = "",
    val airBags: String = "",
    val damage_type: String = "",
    val odometer: Int = 0,
    val location: String = "",
    val fuel_type: String = "",
    val lot_cat: String = "",
    val group_model: String = "",
    val country_code: String = "",
    val claim_number: String? = null,
    val re_request: Boolean = false
)