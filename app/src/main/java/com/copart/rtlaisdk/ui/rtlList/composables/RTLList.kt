package com.copart.rtlaisdk.ui.rtlList.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.copart.rtlaisdk.data.model.RTLListItem
import com.copart.rtlaisdk.data.model.buildRTLListItemPreview

@Composable
fun RTLList(rtlList: List<RTLListItem>, onItemClick: (RTLListItem) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(rtlList) { item ->
                RTLListItem(rtlData = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RTLListScreenPreview() {
    val rtlList = List(5) { buildRTLListItemPreview() }
    RTLList(rtlList = rtlList) {}
}