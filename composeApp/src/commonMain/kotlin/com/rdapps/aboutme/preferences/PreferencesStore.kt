package com.rdapps.aboutme.preferences

import io.github.xxfast.kstore.KStore

expect fun createPreferencesStore(): KStore<Preferences>

