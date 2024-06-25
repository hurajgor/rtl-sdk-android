package com.copart.rtlaisdk.data

import com.copart.rtlaisdk.data.model.RTLListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RTLRepositoryImpl(
    private val rtlApi: RTLApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RTLRepository {

    override suspend fun getRtlList(): Result<RTLListResponse> = makeApiCall(dispatcher) {
        rtlApi.getRTLList()
    }
}

suspend fun <T> makeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
): Result<T> = runCatching {
    withContext(dispatcher) {
        call.invoke()
    }
}