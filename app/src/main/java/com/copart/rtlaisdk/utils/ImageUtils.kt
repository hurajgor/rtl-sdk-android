package com.copart.rtlaisdk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.Locale

fun Uri.toCompressedBitmap(
    context: Context,
    maxWidth: Int,
    maxHeight: Int,
    compressFormat: Bitmap.CompressFormat,
    bitmapQuality: Int
): Bitmap? {
    val contentResolver = context.contentResolver
    var inputStream = contentResolver.openInputStream(this)

    // Decode the bitmap with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream?.close()

    // Calculate the sample size to scale down the bitmap
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false

    // Decode the bitmap with the calculated sample size
    inputStream = contentResolver.openInputStream(this)
    val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
    inputStream?.close()

    // Compress the bitmap
    val outputStream = ByteArrayOutputStream()
    bitmap?.compress(compressFormat, bitmapQuality, outputStream)

    val compressedBitmap =
        BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
    return compressedBitmap
}

fun Bitmap.toRequestBody(compressFormat: Bitmap.CompressFormat, bitmapQuality: Int): RequestBody {
    val outputStream = ByteArrayOutputStream()
    this.compress(compressFormat, bitmapQuality, outputStream)
    val byteArray = outputStream.toByteArray()
    return byteArray.toRequestBody(
        "image/${compressFormat.name.lowercase(Locale.getDefault())}".toMediaTypeOrNull(),
        0,
        byteArray.size
    )
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    maxWidth: Int,
    maxHeight: Int
): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > maxHeight || width > maxWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while (halfHeight / inSampleSize >= maxHeight && halfWidth / inSampleSize >= maxWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}