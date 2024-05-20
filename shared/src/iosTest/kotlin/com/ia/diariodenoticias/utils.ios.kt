package com.ia.diariodenoticias

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

internal actual class SharedFileReader{

    @OptIn(ExperimentalForeignApi::class)
    actual fun loadJsonFile(fileName: String): String? {
        val path = NSBundle.mainBundle.pathForResource(fileName, "json")
        return path?.let {
            NSString.stringWithContentsOfFile(it, NSUTF8StringEncoding, null)
        }
    }
}