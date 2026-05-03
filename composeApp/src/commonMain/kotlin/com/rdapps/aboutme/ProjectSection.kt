package com.rdapps.aboutme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.rdapps.aboutme.components.DemoPlayStopOverlay
import com.rdapps.aboutme.examples.BatteryToolsVisual
import com.rdapps.aboutme.examples.BirthdayCalendarVisual
import com.rdapps.aboutme.examples.CircularListExample
import com.rdapps.aboutme.examples.MotionTextVisual
import com.rdapps.aboutme.examples.ValuePickerSliderExample
import com.rdapps.aboutme.examples.VerticalStepperExample
import com.rdapps.aboutme.examples.ViewSliderExample
import com.rdapps.aboutme.examples.WeddingInvitationVisual
import com.rdapps.aboutme.theme.PortfolioTheme
import com.rdapps.aboutme.utils.LocalIsWideScreen
import com.rdapps.aboutme.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.stringResource
import aboutme.composeapp.generated.resources.Res
import aboutme.composeapp.generated.resources.label_show_less
import aboutme.composeapp.generated.resources.label_show_more
import aboutme.composeapp.generated.resources.label_scroll_top
import aboutme.composeapp.generated.resources.label_link
import aboutme.composeapp.generated.resources.msg_already_looking
import aboutme.composeapp.generated.resources.proj_value_picker_title
import aboutme.composeapp.generated.resources.proj_value_picker_desc
import aboutme.composeapp.generated.resources.proj_view_slider_title
import aboutme.composeapp.generated.resources.proj_view_slider_desc
import aboutme.composeapp.generated.resources.proj_circular_list_title
import aboutme.composeapp.generated.resources.proj_circular_list_desc
import aboutme.composeapp.generated.resources.proj_vertical_stepper_title
import aboutme.composeapp.generated.resources.proj_vertical_stepper_desc
import aboutme.composeapp.generated.resources.proj_wedding_inv_title
import aboutme.composeapp.generated.resources.proj_wedding_inv_desc
import aboutme.composeapp.generated.resources.proj_birthday_cal_title
import aboutme.composeapp.generated.resources.proj_birthday_cal_desc
import aboutme.composeapp.generated.resources.proj_battery_tools_title
import aboutme.composeapp.generated.resources.proj_battery_tools_desc
import aboutme.composeapp.generated.resources.proj_about_me_title
import aboutme.composeapp.generated.resources.proj_about_me_desc
import aboutme.composeapp.generated.resources.proj_motion_text_title
import aboutme.composeapp.generated.resources.proj_motion_text_desc
import aboutme.composeapp.generated.resources.proj_shared_pref_title
import aboutme.composeapp.generated.resources.proj_shared_pref_desc
import aboutme.composeapp.generated.resources.proj_typewriter_title
import aboutme.composeapp.generated.resources.proj_typewriter_desc
import aboutme.composeapp.generated.resources.proj_flow_layouts_title
import aboutme.composeapp.generated.resources.proj_flow_layouts_desc
import aboutme.composeapp.generated.resources.proj_generic_adapter_title
import aboutme.composeapp.generated.resources.proj_generic_adapter_desc
import aboutme.composeapp.generated.resources.proj_uncheckable_radio_title
import aboutme.composeapp.generated.resources.proj_uncheckable_radio_desc
import aboutme.composeapp.generated.resources.proj_social_hub_title
import aboutme.composeapp.generated.resources.proj_social_hub_desc
import aboutme.composeapp.generated.resources.proj_tic_tac_toe_title
import aboutme.composeapp.generated.resources.proj_tic_tac_toe_desc
import aboutme.composeapp.generated.resources.tag_os_library
import aboutme.composeapp.generated.resources.tag_compose
import aboutme.composeapp.generated.resources.tag_kotlin
import aboutme.composeapp.generated.resources.tag_cmp
import aboutme.composeapp.generated.resources.tag_web
import aboutme.composeapp.generated.resources.tag_android
import aboutme.composeapp.generated.resources.tag_wearos
import aboutme.composeapp.generated.resources.tag_material
import aboutme.composeapp.generated.resources.tag_motion
import aboutme.composeapp.generated.resources.tag_osourced
import aboutme.composeapp.generated.resources.tag_glance
import aboutme.composeapp.generated.resources.tag_ios
import aboutme.composeapp.generated.resources.tag_desktop
import aboutme.composeapp.generated.resources.tag_xml
import aboutme.composeapp.generated.resources.tag_datastore
import aboutme.composeapp.generated.resources.tag_sharedpref
import aboutme.composeapp.generated.resources.tag_enum
import aboutme.composeapp.generated.resources.tag_flows
import aboutme.composeapp.generated.resources.tag_enc
import aboutme.composeapp.generated.resources.tag_flex
import org.jetbrains.compose.resources.StringResource


@Stable
data class Project(
    val titleRes: StringResource,
    val descriptionRes: StringResource,
    val tagsRes: List<StringResource>,
    val visual: @Composable (modifier: Modifier, onEvent: (PortfolioScreenEvent) -> Unit) -> Unit = { it, _ ->
        ProjectVisual(
            it,
            stringResource(titleRes)
        )
    },
    val link: String,
    val customFont: FontResource? = null
)

private val projectList = listOf(
    Project(
        titleRes = Res.string.proj_value_picker_title,
        descriptionRes = Res.string.proj_value_picker_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_compose, Res.string.tag_kotlin),
        visual = { it, onEvent ->
            ValuePickerSliderExample(onEvent, it)
        },
        link = "https://github.com/rajdeepvaghela/ValuePickerSlider"
    ),
    Project(
        titleRes = Res.string.proj_view_slider_title,
        descriptionRes = Res.string.proj_view_slider_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_compose, Res.string.tag_kotlin),
        visual = { it, onEvent ->
            ViewSliderExample(onEvent, it)
        },
        link = "https://github.com/rajdeepvaghela/ViewSlider"
    ),
    Project(
        titleRes = Res.string.proj_circular_list_title,
        descriptionRes = Res.string.proj_circular_list_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_compose, Res.string.tag_kotlin),
        visual = { it, onEvent ->
            CircularListExample(onEvent, it)
        },
        link = "https://github.com/rajdeepvaghela/CircularList"
    ),
    Project(
        titleRes = Res.string.proj_vertical_stepper_title,
        descriptionRes = Res.string.proj_vertical_stepper_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_compose, Res.string.tag_kotlin),
        visual = { it, onEvent ->
            VerticalStepperExample(onEvent, it)
        },
        link = "https://github.com/rajdeepvaghela/VerticalStepper"
    ),
    Project(
        titleRes = Res.string.proj_wedding_inv_title,
        descriptionRes = Res.string.proj_wedding_inv_desc,
        tagsRes = listOf(Res.string.tag_cmp, Res.string.tag_kotlin, Res.string.tag_compose, Res.string.tag_web, Res.string.tag_android),
        visual = { it, onEvent ->
            WeddingInvitationVisual(onEvent, it)
        },
        link = "https://github.com/rajdeepvaghela/WeddingInvitation"
    ),
    Project(
        titleRes = Res.string.proj_birthday_cal_title,
        descriptionRes = Res.string.proj_birthday_cal_desc,
        tagsRes = listOf(Res.string.tag_android, Res.string.tag_wearos, Res.string.tag_kotlin, Res.string.tag_material, Res.string.tag_motion),
        visual = { it, _ ->
            BirthdayCalendarVisual(it)
        },
        link = "https://play.google.com/store/apps/details?id=com.rdapps.fbbirthdayfetcher"
    ),
    Project(
        titleRes = Res.string.proj_battery_tools_title,
        descriptionRes = Res.string.proj_battery_tools_desc,
        tagsRes = listOf(Res.string.tag_osourced, Res.string.tag_kotlin, Res.string.tag_compose, Res.string.tag_glance, Res.string.tag_wearos),
        visual = { it, _ ->
            BatteryToolsVisual(it)
        },
        link = "https://github.com/rajdeepvaghela/BatteryTools"
    ),
    Project(
        titleRes = Res.string.proj_about_me_title,
        descriptionRes = Res.string.proj_about_me_desc,
        tagsRes = listOf(
            Res.string.tag_cmp,
            Res.string.tag_kotlin,
            Res.string.tag_compose,
            Res.string.tag_web,
            Res.string.tag_android,
            Res.string.tag_ios,
            Res.string.tag_desktop
        ),
        link = "https://github.com/rajdeepvaghela/AboutMe",
        visual = { it, onEvent ->
            Box(
                modifier = it.fillMaxWidth()
                    .background(
                        PortfolioTheme.colors.secondaryBackground,
                        RoundedCornerShape(40.dp)
                    )
                    .height(if (LocalIsWideScreen.current) 300.dp else 250.dp),
                contentAlignment = Alignment.Center
            ) {
                var isDemoLive by remember { mutableStateOf(false) }

                if (!isDemoLive) {
                    DemoPlayStopOverlay(
                        isDemoLive = isDemoLive,
                        onPlay = {
                            onEvent(
                                PortfolioScreenEvent.TrackEvent(
                                    AppViewModel.Events.ClickLivePreview(
                                        "AboutMe"
                                    )
                                )
                            )
                            isDemoLive = true
                        },
                        onStop = { },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.msg_already_looking),
                        color = PortfolioTheme.colors.primaryText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    ),
    Project(
        titleRes = Res.string.proj_motion_text_title,
        descriptionRes = Res.string.proj_motion_text_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_xml, Res.string.tag_kotlin, Res.string.tag_material, Res.string.tag_motion),
        visual = { it, _ ->
            MotionTextVisual(it)
        },
        link = "https://github.com/rajdeepvaghela/MotionText"
    ),
    Project(
        titleRes = Res.string.proj_shared_pref_title,
        descriptionRes = Res.string.proj_shared_pref_desc,
        tagsRes = listOf(
            Res.string.tag_android,
            Res.string.tag_kotlin,
            Res.string.tag_datastore,
            Res.string.tag_sharedpref,
            Res.string.tag_enum,
            Res.string.tag_flows,
            Res.string.tag_enc
        ),
        link = "https://github.com/rajdeepvaghela/SharedPref"
    ),
    Project(
        titleRes = Res.string.proj_typewriter_title,
        descriptionRes = Res.string.proj_typewriter_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_android, Res.string.tag_xml),
        link = "https://github.com/rajdeepvaghela/TypeWriter"
    ),
    Project(
        titleRes = Res.string.proj_flow_layouts_title,
        descriptionRes = Res.string.proj_flow_layouts_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_android, Res.string.tag_xml, Res.string.tag_flex),
        link = "https://github.com/rajdeepvaghela/FlowLayouts"
    ),
    Project(
        titleRes = Res.string.proj_generic_adapter_title,
        descriptionRes = Res.string.proj_generic_adapter_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_android, Res.string.tag_xml),
        link = "https://github.com/rajdeepvaghela/GenericAdapter"
    ),
    Project(
        titleRes = Res.string.proj_uncheckable_radio_title,
        descriptionRes = Res.string.proj_uncheckable_radio_desc,
        tagsRes = listOf(Res.string.tag_os_library, Res.string.tag_android, Res.string.tag_xml),
        link = "https://github.com/rajdeepvaghela/UncheckableRadioButton"
    ),
    Project(
        titleRes = Res.string.proj_social_hub_title,
        descriptionRes = Res.string.proj_social_hub_desc,
        tagsRes = listOf(Res.string.tag_android, Res.string.tag_kotlin),
        link = "https://play.google.com/store/apps/details?id=com.rdapps.socialhub"
    ),
    Project(
        titleRes = Res.string.proj_tic_tac_toe_title,
        descriptionRes = Res.string.proj_tic_tac_toe_desc,
        tagsRes = listOf(Res.string.tag_android, Res.string.tag_kotlin),
        link = "https://play.google.com/store/apps/details?id=com.rdapps.dotcross"
    )
)

private const val INITIAL_PROJECT_COUNT = 8

@Composable
fun ProjectSection(onEvent: (PortfolioScreenEvent) -> Unit, modifier: Modifier = Modifier) {
    val isWideScreen = LocalIsWideScreen.current
    var showMore by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val visibleProjects = projectList.take(INITIAL_PROJECT_COUNT)
    val hiddenProjects = projectList.drop(INITIAL_PROJECT_COUNT)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 100.dp, horizontal = if (isWideScreen) 40.dp else 24.dp)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            visibleProjects.forEachIndexed { index, project ->
                ProjectItem(project, index, onEvent)
            }

            AnimatedVisibility(
                visible = showMore,
                enter = fadeIn(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(80.dp)) {
                    hiddenProjects.forEachIndexed { index, project ->
                        ProjectItem(project, visibleProjects.size + index, onEvent)
                    }
                }
            }

            // Show More / Show Less button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = PortfolioTheme.colors.background,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, PortfolioTheme.colors.accent),
                    modifier = Modifier.clip(CircleShape).clickable {

                        if (showMore) {
                            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickShowLess))
                        } else {
                            onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickShowMore))
                        }

                        showMore = !showMore

                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (showMore) stringResource(Res.string.label_show_less) else stringResource(Res.string.label_show_more),
                            color = PortfolioTheme.colors.primaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = if (showMore) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = if (showMore) stringResource(Res.string.label_show_less) else stringResource(Res.string.label_show_more),
                            tint = PortfolioTheme.colors.primaryText
                        )
                    }
                }
            }
        }

        // Scroll to top button
        AnimatedVisibility(
            visible = scrollState.value > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
                .systemBarsPadding()
        ) {
            SmallFloatingActionButton(
                onClick = {
                    onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.ClickScrollToTop))

                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                },
                containerColor = PortfolioTheme.colors.accent,
                contentColor = PortfolioTheme.colors.background,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(Res.string.label_scroll_top)
                )
            }
        }
    }
}

@Composable
private fun ProjectItem(project: Project, index: Int, onEvent: (PortfolioScreenEvent) -> Unit) {
    val isWideScreen = LocalIsWideScreen.current
    if (isWideScreen) {
        Row(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (index % 2 == 0) {
                project.visual.invoke(Modifier.weight(1f), onEvent)
                ProjectDetails(project, onEvent, Modifier.weight(1f))
            } else {
                ProjectDetails(project, onEvent, Modifier.weight(1f))
                project.visual.invoke(Modifier.weight(1f), onEvent)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            project.visual.invoke(Modifier.fillMaxWidth(), onEvent)
            ProjectDetails(project, onEvent)
        }
    }
}

@Composable
fun ProjectVisual(modifier: Modifier, title: String) {
    Box(
        modifier = modifier
            .background(PortfolioTheme.colors.background, shape = RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(PortfolioTheme.colors.secondaryBackground)
                .padding(20.dp)
        ) {
            // Background decoration in the card
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )

            // Abstract "phone" and "web" shapes to mimic the screenshots
            Surface(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(0.8f)
                    .align(Alignment.CenterStart),
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {}

            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(0.7f)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp),
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ) {}
        }

        Text(
            text = title,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 20.dp)
        )
    }
}

@Preview
@Composable
fun ProjectVisualPreview() {
    ProjectVisual(Modifier, "Title")
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ProjectDetails(
    project: Project,
    onEvent: (PortfolioScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val title = stringResource(project.titleRes)
        Text(
            text = title,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = project.customFont?.let { FontFamily(Font(it)) }
        )
        Spacer(modifier = Modifier.height(24.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            project.tagsRes.forEach {
                ProjectTag(stringResource(it))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(project.descriptionRes),
            color = PortfolioTheme.colors.secondaryText,
            fontSize = 16.sp,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        val uriHandler = LocalUriHandler.current

        SmallExtendedFloatingActionButton(
            text = {
                Text(stringResource(Res.string.label_link))
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            onClick = {
                onEvent(PortfolioScreenEvent.TrackEvent(AppViewModel.Events.OpenLink(title)))
                uriHandler.openUri(project.link)
            },
            shape = CircleShape,
            containerColor = PortfolioTheme.colors.accent,
            contentColor = PortfolioTheme.colors.background,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        )
    }
}

@Composable
private fun ProjectTag(text: String) {
    Surface(
        color = PortfolioTheme.colors.background,
        shape = CircleShape,
        border = BorderStroke(1.dp, PortfolioTheme.colors.accent)
    ) {
        Text(
            text = text,
            color = PortfolioTheme.colors.primaryText,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}