package com.rdapps.aboutme.examples

import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.ex_stepper_btn_approve
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.PortfolioScreenEvent
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel
import com.rdapps.stepper.Step
import com.rdapps.stepper.StepData
import com.rdapps.stepper.StepState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun VerticalStepperExample(onEvent: (PortfolioScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    var isDemoLive by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .border(1.dp, PortfolioTheme.colors.accent, RoundedCornerShape(40.dp))
                .fillMaxSize()
                .padding(20.dp)
        ) {
            var stageList by remember {
                mutableStateOf(emptyStageList())
            }

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(isDemoLive) {
                stageList = if (isDemoLive) {
                    initialStageList().mapIndexed { index, stage ->
                        if (index == 0)
                            stage.copy(stepState = StepState.InitiallyAnimating)
                        else
                            stage.copy(stepState = StepState.InQueue)
                    }
                } else {
                    emptyStageList()
                }
            }

            key(isDemoLive) {
                val accentStroke = PortfolioTheme.colors.accentStroke

                stageList.forEach {
                    Step(
                        stepData = it,
                        useAlternateComponent = it.id == 2L,
                        accentColor = PortfolioTheme.colors.accentStroke,
                        textColor = PortfolioTheme.colors.primaryText,
                        alternateComponent = {
                            Button(onClick = {
                                coroutineScope.launch {
                                    stageList = stageList.map { stage ->
                                        when (stage.id) {
                                            1L -> stage.copy(stepState = StepState.Done())
                                            2L -> stage.copy(stepState = StepState.Loading)
                                            3L -> stage.copy(stepState = StepState.Visible)
                                            else -> stage
                                        }
                                    }
                                    delay(2000)
                                    stageList = stageList.map { stage ->
                                        when (stage.id) {
                                            1L, 2L -> stage.copy(stepState = StepState.Done())
                                            else -> stage
                                        }
                                    }
                                    delay(1000)
                                    stageList = stageList.map { stage ->
                                        when (stage.id) {
                                            3L -> stage.copy(stepState = StepState.Active(color = accentStroke))
                                            else -> stage
                                        }
                                    }
                                }
                            }) { Text(text = stringResource(Res.string.ex_stepper_btn_approve)) }
                        }
                    ) {
                        val currentElement =
                            stageList.firstOrNull { stage -> stage.stepState == StepState.InitiallyAnimating }
                        val nextElement =
                            stageList.firstOrNull { stage -> stage.stepState == StepState.InQueue }

                        stageList = stageList.map { stage ->
                            if (stage.id == currentElement?.id) stage.copy(stepState = StepState.Visible)
                            else if (stage.id == nextElement?.id) stage.copy(stepState = StepState.InitiallyAnimating)
                            else stage
                        }

                        if (nextElement == null) {
                            coroutineScope.launch {
                                // Spotlight each milestone sequentially once the queue is exhausted.
                                listOf(1L, 2L).forEachIndexed { index, stageId ->
                                    if (index != 0) delay(6000)
                                    stageList = stageList.map { stage ->
                                        if (stage.id == stageId) stage.copy(
                                            stepState = StepState.Active(
                                                color = accentStroke
                                            )
                                        ) else stage
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        DemoPlayStopOverlay(
            isDemoLive = isDemoLive,
            onPlay = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickLivePreview("VerticalStepper")))
                isDemoLive = true
            },
            onStop = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickStopPreview("VerticalStepper")))
                isDemoLive = false
            },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}

private fun initialStageList(): List<StepData> = listOf(
    StepData(
        id = 1L,
        stepState = StepState.InQueue,
        title = "Discovery Workshop",
        bodyText = "Align on problem framing, success metrics, and constraints. Capture personas, north-star metrics, and an initial feature backlog to keep every later step grounded."
    ),
    StepData(
        id = 2L,
        stepState = StepState.InQueue,
        title = "Design Sprint Sign-off",
        bodyText = "Rapidly explore interaction models, validate a clickable prototype with stakeholders, and lock scope for the pilot release so delivery can stay async-friendly."
    ),
    StepData(
        id = 3L,
        stepState = StepState.InQueue,
        title = "Pilot Build & Launch",
        bodyText = "Implement the approved slice inside the Compose Multiplatform stack, add telemetry, and stage a limited beta before handing over launch assets.",
        isLast = true
    )
)

private fun emptyStageList(): List<StepData> = List(3) {
    StepData(
        id = it.toLong(),
        stepState = StepState.InQueue,
        title = "_",
        bodyText = "_\n_\n"
    )
}
