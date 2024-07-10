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
    options: List<String>,
    modifier: Modifier = Modifier,
    fieldName: String = "",
    showHeader: Boolean = false,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isExpandable by remember { mutableStateOf(true) }
    var selectedOption by remember { mutableStateOf("Select Option") }

    // Reset selectedOption when options list changes
    LaunchedEffect(options) {
        selectedOption = "Select Option"
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
                value = selectedOption,
                onValueChange = { onValueSelected(selectedOption) },
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
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedOption = item
                            expanded = false
                            onValueSelected(selectedOption)
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
            options = listOf("Option 1", "Option 2", "Option 3"),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            fieldName = "Field Name",
            showHeader = true,
            onValueSelected = {})
    }
}