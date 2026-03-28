package com.rdapps.aboutme.preferences

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun createPreferencesStore(): KStore<Preferences> {
    val fileManager: NSFileManager = NSFileManager.defaultManager
    val documentsUrl: NSURL = fileManager.URLForDirectory(
        directory = NSDocumentDirectory,
        appropriateForURL = null,
        create = false,
        inDomain = NSUserDomainMask,
        error = null
    )!!
    val path = "${documentsUrl.path}/preferences.json"
    return storeOf(file = Path(path))
}