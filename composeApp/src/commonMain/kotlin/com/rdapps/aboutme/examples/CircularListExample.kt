package com.rdapps.aboutme.examples

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.PortfolioScreenEvent
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel
import com.rdapps.circularlist.CircularList
import com.rdapps.circularlist.InfiniteCircularList
import aboutme.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun CircularListExample(onEvent: (PortfolioScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    var isDemoLive by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier) {
        Row(
            modifier = Modifier
                .border(1.dp, PortfolioTheme.colors.accent, RoundedCornerShape(40.dp))
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            var month by remember { mutableIntStateOf(1) }

            var year by remember { mutableIntStateOf(2024) }

            val monthList = remember {
                listOf(
                    Res.string.ex_circular_month_jan,
                    Res.string.ex_circular_month_feb,
                    Res.string.ex_circular_month_mar,
                    Res.string.ex_circular_month_apr,
                    Res.string.ex_circular_month_may,
                    Res.string.ex_circular_month_jun,
                    Res.string.ex_circular_month_jul,
                    Res.string.ex_circular_month_aug,
                    Res.string.ex_circular_month_sept,
                    Res.string.ex_circular_month_oct,
                    Res.string.ex_circular_month_nov,
                    Res.string.ex_circular_month_dec
                )
            }

            val yearList = remember { (2020..2025).toList() }

            InfiniteCircularList(
                width = 100.dp,
                itemHeight = 40.dp,
                items = monthList.map { stringResource(it) },
                initialItem = stringResource(monthList[month - 1]),
                textColor = PortfolioTheme.colors.secondaryText,
                selectedTextColor = PortfolioTheme.colors.primaryText,
                onItemSelected = { index, _ ->
                    month = index + 1
                }
            )

            CircularList(
                width = 100.dp,
                itemHeight = 40.dp,
                items = yearList,
                initialItem = year,
                textColor = PortfolioTheme.colors.secondaryText,
                selectedTextColor = PortfolioTheme.colors.primaryText,
                onItemSelected = { _, item -> year = item },
                prepareDisplayItem = { "2K ${it % 2000}" }
            )
        }

        DemoPlayStopOverlay(
            isDemoLive = isDemoLive,
            onPlay = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickLivePreview("CircularList")))
                isDemoLive = true
            },
            onStop = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickStopPreview("CircularList")))
                isDemoLive = false
            },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}