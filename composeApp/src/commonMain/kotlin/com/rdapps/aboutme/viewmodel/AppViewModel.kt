package com.rdapps.aboutme.viewmodel

import androidx.lifecycle.ViewModel
import com.rdapps.aboutme.utils.createHttpClient

class AppViewModel : ViewModel() {

    private val client by lazy {
        createHttpClient()
    }

    init {

    }

}