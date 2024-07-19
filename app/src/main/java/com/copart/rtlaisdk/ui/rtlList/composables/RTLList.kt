package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.copart.rtlaisdk.RTLAIApplication
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
    val preferencesManager = remember { PreferencesManager(RTLAIApplication.appContext) }
    val scrollPosition = preferencesManager.get(RTL_LIST_SCROLL_POSITION, 0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = scrollPosition)

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(rtlList) { item ->
            RTLListItem(rtlData = item)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collectLatest {
                preferencesManager.save(RTL_LIST_SCROLL_POSITION, it)
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