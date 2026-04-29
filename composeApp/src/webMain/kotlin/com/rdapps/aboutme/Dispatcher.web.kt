package com.rdapps.aboutme

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.KmpIO: CoroutineDispatcher
    get() = Dispatchers.Default