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
import com.rdapps.aboutme.PortfolioScreenEvent
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel
import com.rdapps.valuepickerslider.BarBreak
import com.rdapps.valuepickerslider.ValuePickerSlider
import com.rdapps.valuepickerslider.rememberSliderState
import org.jetbrains.compose.resources.stringResource
import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.ex_slider_radio_city
import aboutme.composeapp.generated.resources.ex_slider_big_fm
import aboutme.composeapp.generated.resources.ex_slider_red_fm
import aboutme.composeapp.generated.resources.ex_slider_radio_mirchi

@Preview
@Composable
fun ValuePickerSliderExample(
    onEvent: (PortfolioScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {

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
            currentValueLabel = { value ->
                Text(
                    text = when (value) {
                        911 -> stringResource(Res.string.ex_slider_radio_city)
                        927 -> stringResource(Res.string.ex_slider_big_fm)
                        935 -> stringResource(Res.string.ex_slider_red_fm)
                        983 -> stringResource(Res.string.ex_slider_radio_mirchi)
                        else -> "${value / 10f}"
                    },
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
            onPlay = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickLivePreview("ValuePickerSlider")))
                isDemoLive = true
            },
            onStop = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickStopPreview("ValuePickerSlider")))
                isDemoLive = false
            },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}
