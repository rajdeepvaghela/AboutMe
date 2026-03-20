package com.rdapps.aboutme.viewslider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> ViewSlider(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemWidth: Dp = 100.dp,
    scaleDownFactor: Float = 0.5f,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> },
    selectedItemIndicator: @Composable BoxScope.(item: T) -> Unit = {
        Image(
            imageVector = Icons.Default.ArrowDropDown,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = "Selected Item Arrow"
        )
    },
    selectedItemLabel: @Composable BoxScope.(item: T) -> Unit = {},
    content: @Composable BoxScope.(item: T) -> Unit
) {
    val density = LocalDensity.current
    val itemHalfWidth = density.run { itemWidth.toPx() / 2f }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState)

    var lastSelectedIndex by remember {
        mutableIntStateOf(0)
    }

    val velocityTracker = remember { VelocityTracker() }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
            .pointerInput(snapFlingBehavior) {
                detectDragGestures(
                    onDragEnd = {
                        val velocity = velocityTracker.calculateVelocity()
                        velocityTracker.resetTracking()
                        coroutineScope.launch {
                            scrollState.scroll {
                                with(snapFlingBehavior) {
                                    performFling(-velocity.x)
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        velocityTracker.resetTracking()
                        coroutineScope.launch {
                            scrollState.scroll {
                                with(snapFlingBehavior) {
                                    performFling(0f)
                                }
                            }
                        }
                    }
                ) { change, dragAmount ->
                    velocityTracker.addPosition(change.uptimeMillis, change.position)
                    coroutineScope.launch {
                        scrollState.scrollBy(-dragAmount.x)
                    }
                }
            }
    ) {
        var sliderHeight by remember {
            mutableStateOf(0.dp)
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                selectedItemIndicator(items[lastSelectedIndex])
            }

            val parentHalfWidth = density.run {
                this@BoxWithConstraints.maxWidth.toPx() / 2
            }
            val startPoint = parentHalfWidth - (itemHalfWidth * 2)
            val endPoint = parentHalfWidth + (itemHalfWidth * 2)

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        sliderHeight = density.run { it.size.height.toDp() }
                    },
                state = scrollState,
                flingBehavior = snapFlingBehavior,
                contentPadding = PaddingValues(horizontal = this@BoxWithConstraints.maxWidth / 2 - itemWidth / 2)
            ) {
                itemsIndexed(items) { index, item ->
                    var scaleFactor by remember(index) {
                        mutableFloatStateOf(1f)
                    }

                    Box(
                        modifier = Modifier
                            .width(itemWidth)
                            .onGloballyPositioned {
                                val x = it.positionInParent().x + itemHalfWidth
                                val delta = abs(parentHalfWidth - x)

                                scaleFactor = if (x < parentHalfWidth) {
                                    (x - startPoint) / (parentHalfWidth - startPoint)
                                } else {
                                    (endPoint - x) / (endPoint - parentHalfWidth)
                                }.coerceIn(scaleDownFactor, 1f)

                                val isSelected = delta < density.run { itemWidth.toPx() / 2 }

                                if (isSelected && lastSelectedIndex != index) {
                                    onItemSelected(index, item)
                                    lastSelectedIndex = index
                                }
                            }
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    scrollState.animateScrollToItem(index)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.scale(scaleFactor)) {
                            content(item)
                        }
                    }
                }
            }

            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                selectedItemLabel(items[lastSelectedIndex])
            }
        }
    }
}