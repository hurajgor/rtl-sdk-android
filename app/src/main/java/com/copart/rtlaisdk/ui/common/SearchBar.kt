package com.copart.rtlaisdk.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copart.rtlaisdk.ui.theme.CopartBlue
import compose.icons.TablerIcons
import compose.icons.tablericons.Search

@Composable
fun SearchBarWithButton(
    placeholder: String = "",
    searchText: String = "",
    onSearchClicked: (String) -> Unit
) {
    val text = remember { mutableStateOf(searchText) }
    val heightModifier = Modifier.height(70.dp)

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        CustomTextField(
            "",
            placeholder,
            onTextChanged = { text.value = it },
            modifier = heightModifier.weight(0.9f),
            showFieldName = false,
            value = text.value
        )
        IconButton(
            onClick = { onSearchClicked(text.value) }, modifier = Modifier
                .padding(start = 12.dp)
                .size(24.dp)
                .background(CopartBlue, RoundedCornerShape(100.dp))
                .align(Alignment.CenterVertically)
                .weight(0.1f)
        ) {
            Icon(
                TablerIcons.Search,
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarWithButtonPreview() {
    SearchBarWithButton {}
}