package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview
import com.copart.rtlaisdk.ui.base.SIDE_EFFECTS_KEY
import com.copart.rtlaisdk.ui.common.NetworkError
import com.copart.rtlaisdk.ui.common.Progress
import com.copart.rtlaisdk.ui.rtlList.RTLListContract
import com.copart.rtlaisdk.ui.rtlList.RTLListContract.Event.NewRTLRequest
import com.copart.rtlaisdk.ui.theme.CopartBlue
import com.copart.rtlaisdk.ui.theme.labelNormal14
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
