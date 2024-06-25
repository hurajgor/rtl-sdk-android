package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse
import retrofit2.http.POST

interface RTLApi {
    @POST(Endpoints.GET_RTL_LIST)
    suspend fun getRTLList(): RTLListResponse
}