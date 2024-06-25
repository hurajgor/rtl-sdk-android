package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.copart.rtlaisdk.ui.common.NetworkError
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview
import com.copart.rtlaisdk.ui.base.SIDE_EFFECTS_KEY
import com.copart.rtlaisdk.ui.common.Progress
import com.copart.rtlaisdk.ui.rtlList.RTLListContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun RTLListScreen(
    state: RTLListContract.State,
    effectFlow: Flow<RTLListContract.Effect>?,
    onEventSent: (event: RTLListContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RTLListContract.Effect.Navigation) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(R.string.data_is_loaded)

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
            }
        }?.collect()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        it
        when {
            state.isLoading -> Progress()
            state.isError -> NetworkError { onEventSent(RTLListContract.Event.Retry) }
            else -> RTLList(rtlList = state.rtlList) {
                onEventSent(
                    RTLListContract.Event.RTLListItemSelection(
                        it
                    )
                )
            }
        }
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
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {},
    )
}
