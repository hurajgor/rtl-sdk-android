package com.copart.rtlaisdk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.painterResource
import com.copart.rtlaisdk.ui.common.CustomCameraScreen

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val overlayResId = intent.getIntExtra("overlayResId", R.drawable.overlay_01)
        setContent {
            val overlay = painterResource(id = overlayResId)
            CustomCameraScreen(
                currentOverlayImage = overlay,
                onImageCaptured = { uri ->
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("capturedImageUri", uri.toString())
                    })
                    finish()
                }
            )
        }
    }
}