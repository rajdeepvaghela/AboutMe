package com.rdapps.aboutme.utils

import androidx.compose.ui.Modifier

inline fun Modifier.doIf(
    condition: Boolean,
    ifFalse: Modifier.() -> Modifier = { this },
    ifTrue: Modifier.() -> Modifier
): Modifier = if (condition) this.ifTrue() else this.ifFalse()

inline fun <T> Modifier.doIfNotNull(obj: T?, block: Modifier.(obj: T) -> Modifier) =
    if (obj != null) this.block(obj) else this