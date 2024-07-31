package com.copart.rtlaisdk.ui.rtlList

import androidx.lifecycle.viewModelScope
import com.copart.rtlaisdk.data.RTLRepository
import com.copart.rtlaisdk.data.model.GetRTLListRequest
import com.copart.rtlaisdk.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class RTLListViewModel(private val rtlRepository: RTLRepository) :
    BaseViewModel<RTLListContract.Event, RTLListContract.State, RTLListContract.Effect>() {

    init {
        getRtlList()
    }

    override fun setInitialState() = RTLListContract.State(
        rtlList = emptyList(),
        rtlDetails = null,
        isLoading = false,
        isError = false,
        start = 0,
        rows = 20,
        maxItems = Int.MAX_VALUE,
        searchText = ""
    )

    override fun handleEvents(event: RTLListContract.Event) {
        when (event) {
            is RTLListContract.Event.RTLListItemSelection -> {
                getRTLDetails(event.rtlListItem.requestId.toString())
            }

            is RTLListContract.Event.Retry -> {
                setState {
                    copy(
                        start = 0,
                        rtlList = emptyList(),
                        maxItems = Int.MAX_VALUE,
                        rtlDetails = null
                    )
                }
                getRtlList()
            }

            RTLListContract.Event.NewRTLRequest -> setEffect {
                RTLListContract.Effect.Navigation.ToVINDecode
            }

            RTLListContract.Event.LoadMoreItems -> {
                if (!viewState.value.isLoading && !viewState.value.isError && viewState.value.rtlList.size < viewState.value.maxItems) {
                    val newStart = viewState.value.start + viewState.value.rows
                    loadMoreItems(newStart)
                }
            }

            is RTLListContract.Event.Search -> {
                setState {
                    copy(
                        start = 0,
                        rtlList = emptyList(),
                        maxItems = Int.MAX_VALUE,
                        searchText = event.searchText
                    )
                }
                getRtlList()
            }
        }
    }

    private fun loadMoreItems(newStart: Int) {
        viewModelScope.launch {
            setState { copy(start = newStart) }
            getRtlList()
        }
    }

    private fun getRTLDetails(requestId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }

            rtlRepository.getRTLDetails(requestId)
                .onSuccess { response ->
                    setState {
                        copy(
                            rtlDetails = response.body,
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

    private fun getRtlList() {
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }
            val request = GetRTLListRequest(
                start = viewState.value.start,
                rows = viewState.value.rows,
                secondarySearch = viewState.value.searchText
            )
            rtlRepository.getRtlList(request)
                .onSuccess { response ->
                    val newItems = response.body?.response?.docs ?: emptyList()
                    setState {
                        copy(
                            rtlList = viewState.value.rtlList + newItems,
                            isLoading = false,
                            maxItems = response.body?.response?.numFound ?: Int.MAX_VALUE
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