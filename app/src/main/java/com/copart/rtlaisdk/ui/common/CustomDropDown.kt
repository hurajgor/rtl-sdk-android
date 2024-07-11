package com.copart.rtlaisdk.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copart.rtlaisdk.ui.theme.labelBold16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(
    options: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    fieldName: String = "",
    showHeader: Boolean = false,
    onValueSelected: (String, String) -> Unit,
    selectedValue: String = "Select Option",
    selectedKey: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(true) }
    var selectedOptionKey by remember { mutableStateOf(selectedKey) }
    var selectedOptionValue by remember { mutableStateOf(selectedValue) }

    // Reset selectedOption when options list changes
    LaunchedEffect(options) {
        selectedOptionValue = selectedValue
        selectedOptionKey = selectedKey
        isExpandable = options.isNotEmpty()
    }

    Column(
        modifier = modifier
    ) {
        if (showHeader) {
            Text(
                text = fieldName,
                style = labelBold16,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (isExpandable) {
                    expanded = !expanded
                }
            }
        ) {
            TextField(
                value = selectedOptionValue,
                onValueChange = { onValueSelected(selectedOptionKey, selectedOptionValue) },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (key, value) ->
                    DropdownMenuItem(
                        text = { Text(text = value) },
                        onClick = {
                            selectedOptionKey = key
                            selectedOptionValue = value
                            expanded = false
                            onValueSelected(selectedOptionKey, selectedOptionValue)
                        }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CustomDropDownPreview() {
    Column {
        CustomDropDown(
            options = listOf(Pair("1", "Option 1"), Pair("2", "Option 2"), Pair("3", "Option 3")),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            fieldName = "Field Name",
            showHeader = true,
            onValueSelected = { key, value -> })
    }
}