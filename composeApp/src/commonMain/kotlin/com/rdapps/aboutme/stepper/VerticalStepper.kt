package com.rdapps.aboutme.stepper

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.doIf
import kotlinx.coroutines.delay
import kotlin.reflect.KClass

sealed class AnimationElement(open val isDone: Boolean) {
    data class Indicator(override val isDone: Boolean = false) : AnimationElement(isDone)
    data class IndicatorProgress(override val isDone: Boolean = false) : AnimationElement(isDone)
    data class Title(override val isDone: Boolean = false) : AnimationElement(isDone)
}

@Composable
fun Step(
    stepData: StepData,
    modifier: Modifier = Modifier,
    isStatic: Boolean = false,
    useAlternateComponent: Boolean = false,
    alternateComponent: @Composable () -> Unit = {},
    onAnimationDone: (isLast: Boolean) -> Unit = {},
) {
    var animationSequence by remember(stepData.id) {
        mutableStateOf(
            if (stepData.isLast) {
                listOf(
                    AnimationElement.Indicator(),
                    AnimationElement.Title()
                )
            } else {
                listOf(
                    AnimationElement.Indicator(),
                    AnimationElement.Title(),
                    AnimationElement.IndicatorProgress()
                )
            }
        )
    }

    fun <T : AnimationElement> KClass<T>.markAsDone() {
        animationSequence = animationSequence.map {
            if (it::class == this) {
                when (it) {
                    is AnimationElement.Indicator -> it.copy(isDone = true)
                    is AnimationElement.IndicatorProgress -> it.copy(isDone = true)
                    is AnimationElement.Title -> it.copy(isDone = true)
                }
            } else {
                it
            }
        }
    }

    val currentAnimationElement by remember(animationSequence) {
        derivedStateOf { animationSequence.firstOrNull { !it.isDone } }
    }

    LaunchedEffect(currentAnimationElement) {
        if (currentAnimationElement == null && stepData.stepState == StepState.InitiallyAnimating) {
            onAnimationDone(stepData.isLast)
        }
    }

    val density = LocalDensity.current
    var isContentExpanding by remember { mutableStateOf(true) }
    var contentHeightPx by remember { mutableStateOf(0) }
    val animatedContentHeightPx by animateIntAsState(
        targetValue = contentHeightPx,
        animationSpec = tween(easing = LinearOutSlowInEasing)
    )

    Row(
        modifier = modifier.fillMaxWidth().doIf(isContentExpanding) {
            animateContentSize()
        }
    ) {
        // Left indicator section
        Column(
            modifier = Modifier.height(with(density) { animatedContentHeightPx.toDp() })
                .doIf(!isContentExpanding) {
                    animateContentSize()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IndicatorIcon(
                stepState = stepData.stepState,
                toBeAnimated = currentAnimationElement is AnimationElement.Indicator
            ) {
                AnimationElement.Indicator::class.markAsDone()
            }

            if (!stepData.isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.TopCenter
                ) {

                    val heightPercent = remember {
                        Animatable(0f)
                    }

                    LaunchedEffect(currentAnimationElement) {
                        if (stepData.stepState == StepState.InitiallyAnimating && currentAnimationElement is AnimationElement.IndicatorProgress) {
                            heightPercent.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 1000)
                            )

                            AnimationElement.IndicatorProgress::class.markAsDone()
                        }
                    }

                    LaunchedEffect(isStatic) {
                        if (isStatic)
                            heightPercent.snapTo(1f)
                    }

                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(heightPercent.value)
                            .background(color = PortfolioTheme.colors.accentStroke)
                    )

                    val animateHeight by animateFloatAsState(
                        targetValue = if (stepData.stepState is StepState.Done || stepData.stepState == StepState.Error)
                            1f
                        else
                            0f,
                        label = "Done Progress",
                        animationSpec = tween(durationMillis = 1000)
                    )

                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .fillMaxHeight(animateHeight)
                            .background(color = Color.Green)
                    )
                }
            }
        }

        // Title and content
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
                .onGloballyPositioned {
                    if (it.size.height != contentHeightPx) {
                        isContentExpanding = it.size.height > contentHeightPx
                    }
                    contentHeightPx = it.size.height
                }
        ) {
            var animatedTitle by remember {
                mutableStateOf("")
            }

            LaunchedEffect(stepData.title, animationSequence) {
                if (currentAnimationElement is AnimationElement.Title && stepData.stepState == StepState.InitiallyAnimating) {
                    animatedTitle = ""

                    stepData.title.forEach {
                        animatedTitle = animatedTitle.removeSuffix("|") + it.plus("|")
                        delay(25)
                    }

                    animatedTitle = animatedTitle.removeSuffix("|")
                    AnimationElement.Title::class.markAsDone()
                } else {
                    when (stepData.stepState) {
                        is StepState.Active,
                        is StepState.Done,
                        StepState.Error,
                        StepState.Loading,
                        StepState.Visible -> {
                            animatedTitle = stepData.title
                        }

                        StepState.InitiallyAnimating -> {
                            // ignore
                        }

                        StepState.InQueue -> {
                            animatedTitle = ""
                        }
                    }
                }
            }

            LaunchedEffect(isStatic) {
                if (isStatic)
                    animatedTitle = stepData.title
            }

            Text(
                text = animatedTitle,
                style = MaterialTheme.typography.labelLarge,
                color = PortfolioTheme.colors.primaryText,
                modifier = Modifier.fillMaxWidth()
            )

            if (stepData.bodyText.isNotBlank() || useAlternateComponent) {
                var animatedBodyText by remember {
                    mutableStateOf("")
                }

                var isVisible by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(stepData.stepState, stepData.bodyText) {
                    when (stepData.stepState) {
                        is StepState.Active,
                        StepState.Loading -> {

                            if (useAlternateComponent) {
                                isVisible = true
                            } else {
                                animatedBodyText = ""

                                stepData.bodyText.forEach {
                                    animatedBodyText =
                                        animatedBodyText.removeSuffix("|") + it.plus("|")
                                    delay(25)
                                }

                                animatedBodyText = animatedBodyText.removeSuffix("|")
                            }
                        }

                        is StepState.Done -> {
                            if (stepData.stepState.showBodyContent) {
                                if (useAlternateComponent) {
                                    isVisible = true
                                } else {
                                    animatedBodyText = ""

                                    stepData.bodyText.forEach {
                                        animatedBodyText =
                                            animatedBodyText.removeSuffix("|") + it.plus("|")
                                        delay(25)
                                    }

                                    animatedBodyText = animatedBodyText.removeSuffix("|")
                                }
                            } else {
                                if (useAlternateComponent) {
                                    isVisible = false
                                } else {
                                    animatedBodyText = ""
                                }
                            }
                        }

                        StepState.InQueue,
                        StepState.InitiallyAnimating,
                        StepState.Visible,
                        StepState.Error -> {
                            if (useAlternateComponent) {
                                isVisible = false
                            } else {
                                animatedBodyText = ""
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (useAlternateComponent) {
                    if (isVisible) {
                        alternateComponent()
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        @Composable
                        fun getTextView(text: String, isVisible: Boolean) = Text(
                            text = text,
                            style = MaterialTheme.typography.bodySmall,
                            color = PortfolioTheme.colors.primaryText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(if (isVisible) 1f else 0f)
                        )

                        if (animatedBodyText.isNotBlank()) {
                            getTextView(text = stepData.bodyText, isVisible = false)

                            getTextView(text = animatedBodyText, isVisible = true)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
