package com.copart.rtlaisdk.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.copart.rtlaisdk.ui.theme.labelBold16
import com.copart.rtlaisdk.ui.theme.labelNormal14

@Composable
fun CustomTextField(
    fieldName: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit,
    trailingIcon: @Composable() (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    textStyle: TextStyle = labelNormal14,
    readOnly: Boolean = false,
    value: String = "",
    showFieldName: Boolean = true
) {

    val text = remember { mutableStateOf(value) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (showFieldName) {
            Text(
                text = fieldName,
                style = labelBold16,
                modifier = modifier.padding(top = 12.dp)
            )
        }
        TextField(
            value = text.value,
            onValueChange = { newValue ->
                text.value = newValue
                onTextChanged(newValue)
            },
            readOnly = readOnly,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            trailingIcon = trailingIcon,
            placeholder = { Text(placeholder) },
            textStyle = textStyle,
            singleLine = true,
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(fieldName = "Field Name", placeholder = "Placeholder", onTextChanged = {})
}