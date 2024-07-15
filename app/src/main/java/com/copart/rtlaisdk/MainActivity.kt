package com.copart.rtlaisdk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.copart.rtlaisdk.data.model.RTLClientParams
import com.copart.rtlaisdk.ui.navigation.AppNavigation
import com.copart.rtlaisdk.ui.theme.RTLAISDKTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = intent.getStringExtra("params")
        if (!params.isNullOrEmpty()) {
            RTLAIApplication.rtlClientParams = Gson().fromJson(params, RTLClientParams::class.java)
        }
        setContent {
            RTLAISDKTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation { requestId, isSuccess ->
                        Log.d(
                            "RTLSDK",
                            "*** requestId: $requestId, isSuccess: $isSuccess"
                        )
                        val intent = Intent().apply {
                            putExtra("requestId", requestId)
                            putExtra("isSuccess", isSuccess)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}