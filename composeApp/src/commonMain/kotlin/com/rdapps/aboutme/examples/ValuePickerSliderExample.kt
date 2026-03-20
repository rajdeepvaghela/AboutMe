package com.rdapps.aboutme.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.PortfolioTheme
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.valuepickerslider.BarBreak
import com.rdapps.aboutme.valuepickerslider.ValuePickerSlider
import com.rdapps.aboutme.valuepickerslider.rememberSliderState

@Preview
@Composable
fun ValuePickerSliderExample(modifier: Modifier = Modifier) {

    var isDemoLive by rememberSaveable { mutableStateOf(false) }

    val sliderState = rememberSliderState(
        range = 880..1080,
        currentValue = 910f,
        barBreaks = listOf(
            BarBreak(10, 1f),
            BarBreak(5, .75f),
            BarBreak(1, .5f)
        )
    )

    BoxWithConstraints(
        modifier = modifier
    ) {
        ValuePickerSlider(
            state = sliderState,
            numSegments = maxWidth.value.toInt() * 32 / 500,
            currentValueLabel = {
                Text(
                    text = "${it / 10f}",
                    color = PortfolioTheme.colors.primaryText,
                    modifier = Modifier.background(PortfolioTheme.colors.background)
                )
            },
            indicatorLabel = {
                if (it % 10 == 0) {
                    Text(
                        text = "${it / 10f}", color = PortfolioTheme.colors.secondaryText
                    )
                }
            },
            currentBarColor = Color.Red,
            modifier = modifier
                .border(
                    1.dp,
                    PortfolioTheme.colors.accent,
                    RoundedCornerShape(40.dp)
                )
                .padding(horizontal = 20.dp, vertical = 60.dp)
                .fillMaxWidth()
        )

        DemoPlayStopOverlay(
            isDemoLive = isDemoLive,
            onPlay = { isDemoLive = true },
            onStop = { isDemoLive = false },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}