package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.RTLDetailsResponseBody
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview
import com.copart.rtlaisdk.ui.base.SIDE_EFFECTS_KEY
import com.copart.rtlaisdk.ui.common.NetworkError
import com.copart.rtlaisdk.ui.common.Progress
import com.copart.rtlaisdk.ui.common.SearchBarWithButton
import com.copart.rtlaisdk.ui.rtlList.RTLListContract
import com.copart.rtlaisdk.ui.rtlList.RTLListContract.Event.NewRTLRequest
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.labelNormal14
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RTLListScreen(
    state: RTLListContract.State,
    effectFlow: Flow<RTLListContract.Effect>?,
    onEventSent: (event: RTLListContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RTLListContract.Effect.Navigation) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(R.string.data_is_loaded)

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(SIDE_EFFECTS_KEY) {
            onEventSent(RTLListContract.Event.Retry)
            pullRefreshState.endRefresh()
        }
    }

    BackHandler(enabled = state.rtlDetails != null) {
        onEventSent(RTLListContract.Event.Retry)
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is RTLListContract.Effect.DataWasLoaded -> {
                    snackBarHostState.showSnackbar(
                        message = snackBarMessage,
                        duration = SnackbarDuration.Short
                    )
                }

                is RTLListContract.Effect.Navigation.ToRTLDetails -> onNavigationRequested(effect)
                is RTLListContract.Effect.Navigation.ToVINDecode -> onNavigationRequested(effect)
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEventSent(NewRTLRequest) },
                modifier = Modifier.padding(16.dp), // Add padding around the FAB
                shape = ButtonDefaults.shape,
                containerColor = CopartBlue,
                contentColor = Color.White
            ) {
                Text(
                    text = stringResource(R.string.determine_new_rtl),
                    modifier = Modifier.padding(8.dp),
                    style = labelNormal14
                )
            }
        }
    ) {
        it
        when {
            state.isLoading -> Progress()
            state.isError -> NetworkError { onEventSent(RTLListContract.Event.Retry) }
            state.rtlDetails != null -> {
                RTLDetailsText(rtlDetails = state.rtlDetails)
            }
            else ->
                Box(modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
                    Column {
                        SearchBarWithButton(
                            placeholder = stringResource(R.string.rtl_list_search_placeholder),
                            searchText = state.searchText,
                            onSearchClicked = { searchText ->
                                onEventSent(RTLListContract.Event.Search(searchText))
                            })
                        RTLList(rtlList = state.rtlList, onItemClick = { item ->
                            onEventSent(
                                RTLListContract.Event.RTLListItemSelection(
                                    item
                                )
                            )
                        }, onEndReached = { onEventSent(RTLListContract.Event.LoadMoreItems) })
                    }
                    PullToRefreshContainer(
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
        }
    }
}

@Composable
fun RTLDetailsText(rtlDetails: RTLDetailsResponseBody) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "VIN: ${rtlDetails.vin}", style = labelNormal14)
        Text(text = "Claim Number: ${rtlDetails.claimNumber}", style = labelNormal14)
        Text(text = "Description: ${rtlDetails.description}", style = labelNormal14)
        Text(text = "Fuel Type: ${rtlDetails.fuelType}", style = labelNormal14)
        Text(text = "Location: ${rtlDetails.location}", style = labelNormal14)
        Text(text = "Threshold: ${rtlDetails.threshold}", style = labelNormal14)
        Text(text = "Is EOTL: ${rtlDetails.isEOTL}", style = labelNormal14)
        Text(text = "Confidence Label: ${rtlDetails.confidenceLabel}", style = labelNormal14)
        Text(text = "Final Damage Score: ${rtlDetails.finalDamageScore}", style = labelNormal14)
        Text(
            text = "Final Non-Damage Score: ${rtlDetails.finalNonDamageScore}",
            style = labelNormal14
        )
        Text(
            text = "Flipped Confidence Label: ${rtlDetails.flippedConfidenceLabel}",
            style = labelNormal14
        )
        Text(
            text = "Flipped Final Damage Score: ${rtlDetails.flippedFinalDamageScore}",
            style = labelNormal14
        )
        Text(
            text = "Flipped Final Non-Damage Score: ${rtlDetails.flippedFinalNonDamageScore}",
            style = labelNormal14
        )
        Text(
            text = "Total Loss Review Status: ${rtlDetails.totalLossReviewStatus}",
            style = labelNormal14
        )
        Text(
            text = "Total Loss Review Description: ${rtlDetails.totalLossReviewDesc}",
            style = labelNormal14
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenSuccessPreview() {
    val users = List(5) { buildRTLListItemPreview() }
    RTLListScreen(
        state = RTLListContract.State(
            rtlList = users,
            isLoading = false,
            isError = false,
            start = 0,
            rows = 20,
            maxItems = Int.MAX_VALUE,
            searchText = "",
            rtlDetails = null
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {},
    )
}

@Preview(showBackground = true)
@Composable
fun UsersScreenErrorPreview() {
    RTLListScreen(
        state = RTLListContract.State(
            rtlList = emptyList(),
            isLoading = false,
            isError = true,
            start = 0,
            rows = 20,
            maxItems = Int.MAX_VALUE,
            searchText = "",
            rtlDetails = null
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {},
    )
}
