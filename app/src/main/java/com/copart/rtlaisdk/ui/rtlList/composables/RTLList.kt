package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.copart.rtlaisdk.R
import com.copart.rtlaisdk.data.model.RTLListItem
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview
import com.copart.rtlaisdk.data.prefs.PreferencesManager
import com.copart.rtlaisdk.data.prefs.PreferencesManager.Companion.RTL_LIST_SCROLL_POSITION
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RTLList(
    rtlList: List<RTLListItem>,
    onItemClick: (RTLListItem) -> Unit,
    onEndReached: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scrollPosition = preferencesManager?.get(RTL_LIST_SCROLL_POSITION, 0) ?: 0
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = scrollPosition)

    if (rtlList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.no_data_found))
        }
    } else {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            items(rtlList) { item ->
                RTLListItem(rtlData = item)
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collectLatest {
                preferencesManager?.save(RTL_LIST_SCROLL_POSITION, it)
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= rtlList.size - 1) {
                    onEndReached()
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun RTLListScreenPreview() {
    val rtlList = List(5) { buildRTLListItemPreview() }
    RTLList(rtlList = rtlList, onItemClick = {}, onEndReached = {})
}