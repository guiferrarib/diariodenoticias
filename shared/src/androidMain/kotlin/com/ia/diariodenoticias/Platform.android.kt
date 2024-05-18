package com.ia.diariodenoticias

import android.content.res.Resources
import android.os.Build
import android.util.Log
import kotlin.math.round

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Platform() {
    actual val osName: String
        get() = "Android"
    actual val osVersion: String
        get() = "${Build.VERSION.SDK_INT}"
    actual val deviceModel: String
        get() = "${Build.MANUFACTURER} ${Build.MODEL}"
    actual val density: Int
        get() = round(Resources.getSystem().displayMetrics.density).toInt()

    actual fun logSystemInfo() {
        Log.d(
            "DiarioDeNoticias",
            "($osName, $osVersion, $deviceModel, $density)"
        )
    }
}

actual fun getPlatform(): Platform {
   return Platform()
}