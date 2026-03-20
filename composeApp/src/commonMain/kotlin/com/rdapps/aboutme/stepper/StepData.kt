package com.rdapps.aboutme.stepper

data class StepData(
    val id: Long,
    val stepState: StepState,
    val title: String,
    val bodyText: String = "",
    val isLast: Boolean = false
)