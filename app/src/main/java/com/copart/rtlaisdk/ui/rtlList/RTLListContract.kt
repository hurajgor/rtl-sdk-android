package com.copart.rtlaisdk.ui.rtlList

import com.copart.rtlaisdk.data.model.RTLListItem
import com.copart.rtlaisdk.ui.base.ViewEvent
import com.copart.rtlaisdk.ui.base.ViewSideEffect
import com.copart.rtlaisdk.ui.base.ViewState

class RTLListContract {

    sealed class Event : ViewEvent {
        object Retry : Event()
        data class RTLListItemSelection(val rtlListItem: RTLListItem) : Event()
        object NewRTLRequest : Event()
    }

    data class State(
        val rtlList: List<RTLListItem>,
        val isLoading: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToRTLDetails(val requestId: String) : Navigation()
            object ToVINDecode : Navigation()
        }
    }

}