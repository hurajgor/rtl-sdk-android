package com.copart.rtlaisdk.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun buildAnnotatedStringWithBoldDynamicValue(
    staticText: String,
    dynamicValue: String
): AnnotatedString {
    return buildAnnotatedString {
        append(staticText)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(" $dynamicValue") // Add space before dynamic value
        }
    }
}