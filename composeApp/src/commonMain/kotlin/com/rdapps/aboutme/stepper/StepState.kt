package com.rdapps.aboutme.stepper

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color


sealed class StepState {
    data object InQueue : StepState()
    data object InitiallyAnimating : StepState()
    data object Visible : StepState()
    data class Active(
        @FloatRange(from = 0.0, to = 1.0) val progress: Float = 0f,
        val color: Color = Color.Black
    ) : StepState()

    data object Loading : StepState()
    data class Done(val showBodyContent: Boolean = false) : StepState()
    data object Error : StepState()
}
