package com.rdapps.aboutme.examples

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rdapps.aboutme.PortfolioScreenEvent
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.viewmodel.AppViewModel
import com.rdapps.viewslider.ViewSlider

data class Item(
    val icon: ImageVector,
    val name: String,
    val selectedColor: Color
)

@Composable
fun ViewSliderExample(onEvent: (PortfolioScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    var isDemoLive by rememberSaveable { mutableStateOf(false) }

    val list = listOf(
        Item(Icons.Filled.AccountCircle, "Profile Info", Color(0xFFE57373)),
        Item(Icons.Filled.ShoppingCart, "Cart", Color(0xFF81C784)),
        Item(Icons.Filled.Favorite, "Favorites", Color(0xFF64B5F6)),
        Item(Icons.Filled.Settings, "Settings", Color(0xFFFFD54F))
    )

    var selectedItem by remember { mutableStateOf(list.first()) }

    BoxWithConstraints(modifier = modifier) {
        ViewSlider(
            items = list,
            scaleDownFactor = 0.4f,
            itemWidth = 80.dp,
            selectedItemIndicator = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    tint = it.selectedColor,
                    contentDescription = null
                )
            },
            selectedItemLabel = {
                Text(text = it.name, color = it.selectedColor)
            },
            onItemSelected = { _, item -> selectedItem = item },
            modifier = Modifier
                .border(1.dp, PortfolioTheme.colors.accent, RoundedCornerShape(40.dp))
                .padding(horizontal = 20.dp, vertical = 60.dp)
                .fillMaxWidth()
        ) {
            Image(
                imageVector = it.icon,
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (it == selectedItem) it.selectedColor else PortfolioTheme.colors.secondaryText
                ),
                modifier = Modifier.size(72.dp)
            )
        }

        DemoPlayStopOverlay(
            isDemoLive = isDemoLive,
            onPlay = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickLivePreview("ViewSlider")))
                isDemoLive = true
            },
            onStop = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickStopPreview("ViewSlider")))
                isDemoLive = false
            },
            modifier = Modifier.matchParentSize(),
            overlayShape = RoundedCornerShape(40.dp)
        )
    }
}