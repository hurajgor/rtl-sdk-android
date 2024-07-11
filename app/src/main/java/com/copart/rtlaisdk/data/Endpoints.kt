package com.copart.rtlaisdk.data

object Endpoints {
    const val BASE_URL = "https://c-sellermobile-qa4.copart.com/"

    const val GET_RTL_LIST = "adjuster/api/v1/otl/list"

    const val GET_VEHICLE_YEARS = "adjuster/data/v2/years"

    const val GET_VEHICLE_MAKES = "/adjuster/scs/data/v2/makes/{vehicleType}"

    const val GET_VEHICLE_MODELS = "adjuster/scs/data/v2/models/{vehicleMake}"

    const val VIN_DECODE = "adjuster/data/v1/vin-decode/{vin}"

    const val SELLERS_LIST = "adjuster/scs/v1/sellers"

    const val PRIMARY_DAMAGES_LIST = "adjuster/data/v1/primary-damages"

    const val UPLOAD_RTL = "adjuster/api/v1/otl/upload"
}