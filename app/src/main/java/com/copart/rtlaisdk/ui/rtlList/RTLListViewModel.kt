package com.copart.rtlaisdk.ui.rtlList

import androidx.lifecycle.viewModelScope
import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class RTLListViewModel(private val rtlRepository: RTLRepository) :
    BaseViewModel<RTLListContract.Event, RTLListContract.State, RTLListContract.Effect>() {

    init {
        getRtlList()
    }

    override fun setInitialState() = RTLListContract.State(
        rtlList = emptyList(),
        isLoading = true,
        isError = false,
    )

    override fun handleEvents(event: RTLListContract.Event) {
        when (event) {
            is RTLListContract.Event.RTLListItemSelection -> setEffect {
                RTLListContract.Effect.Navigation.ToRTLDetails(
                    event.rtlListItem.requestId.toString()
                )
            }

            is RTLListContract.Event.Retry -> getRtlList()
        }
    }

    fun getRtlList() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getRtlList()
                .onSuccess { response ->
                    setState {
                        copy(
                            rtlList = response.body?.response?.docs ?: emptyList(),
                            isLoading = false
                        )
                    }
                    setEffect { RTLListContract.Effect.DataWasLoaded }
                }
                .onFailure {
                    setState { copy(isError = true, isLoading = false) }
                }
        }
    }
}