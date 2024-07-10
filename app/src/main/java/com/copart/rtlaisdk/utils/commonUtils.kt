package com.copart.rtlaisdk.utils

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.content.FileProvider
import com.copart.rtlaisdk.BuildConfig
import java.io.File

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

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun getWindowHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun getWindowWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun Context.createTempPictureUri(
    provider: String = "${BuildConfig.APPLICATION_ID}.fileprovider",
    fileName: String = "picture_${System.currentTimeMillis()}",
    fileExtension: String = ".png"
): Uri {
    val tempFile = File(cacheDir, "${fileName}.${fileExtension}")
    return FileProvider.getUriForFile(this, provider, tempFile)
}

fun Modifier.overrideParentHorizontalPadding(padding: Int) =
    layout { measurable, constraints ->
        val noPaddingConstraints = constraints.copy(
            maxWidth = constraints.maxWidth + (padding * 2).toPx,
        )
        val placeable = measurable.measure(noPaddingConstraints)
        layout(placeable.width, placeable.height) {
            if (placeable.width > constraints.maxWidth) {
                placeable.place(x = 0, y = 0)
            } else {
                placeable.place(x = -padding, y = 0)
            }
        }
    }