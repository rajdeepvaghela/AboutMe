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
import com.rdapps.aboutme.circularlist.CircularList
import com.rdapps.aboutme.circularlist.InfiniteCircularList
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.theme.PortfolioTheme

@Composable
fun CircularListExample(modifier: Modifier = Modifier) {
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
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sept",
                    "Oct",
                    "Nov",
                    "Dec"
                )
            }

            val yearList = remember { (2020..2025).toList() }

            InfiniteCircularList(
                width = 100.dp,
                itemHeight = 40.dp,
                items = monthList,
                initialItem = monthList[month - 1],
                textColor = PortfolioTheme.colors.secondaryText,
                selectedTextColor = PortfolioTheme.colors.primaryText,
                onItemSelected = { _, item ->
                    month = monthList.indexOf(item) + 1
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
            onPlay = { isDemoLive = true },
            onStop = { isDemoLive = false },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}