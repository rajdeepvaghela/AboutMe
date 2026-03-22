package com.rdapps.aboutme.examples

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
import com.rdapps.aboutme.PortfolioTheme
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.stepper.Step
import com.rdapps.aboutme.stepper.StepData
import com.rdapps.aboutme.stepper.StepState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun VerticalStepperExample(modifier: Modifier = Modifier) {
    var isDemoLive by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .border(1.dp, PortfolioTheme.colors.accent, RoundedCornerShape(40.dp))
                .fillMaxSize()
                .padding(20.dp)
        ) {
            var stageList by remember {
                mutableStateOf(initialStageList())
            }

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(isDemoLive) {
                stageList = if (isDemoLive) {
                    stageList.mapIndexed { index, stage ->
                        if (index == 0)
                            stage.copy(stepState = StepState.InitiallyAnimating)
                        else
                            stage.copy(stepState = StepState.InQueue)
                    }
                } else {
                    initialStageList()
                }
            }

            key(isDemoLive) {
                stageList.forEach {
                    Step(
                        stepData = it,
                        useAlternateComponent = it.id == 2L,
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
                                            3L -> stage.copy(stepState = StepState.Active())
                                            else -> stage
                                        }
                                    }
                                }
                            }) { Text(text = "Approve designs") }
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
                                        if (stage.id == stageId) stage.copy(stepState = StepState.Active()) else stage
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
            onPlay = { isDemoLive = true },
            onStop = { isDemoLive = false },
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
