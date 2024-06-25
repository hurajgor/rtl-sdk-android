package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse

interface RTLRepository {
    suspend fun getRtlList(): Result<RTLListResponse>
}