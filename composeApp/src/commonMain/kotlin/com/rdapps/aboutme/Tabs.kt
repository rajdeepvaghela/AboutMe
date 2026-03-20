package com.rdapps.aboutme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TabRow() {
//    Box(
//        modifier = Modifier
//            .padding(horizontal = 24.dp)
//            .clip(RoundedCornerShape(100))
//            .background(PortfolioTheme.colors.sidebar)
//            .padding(4.dp)
//    ) {
//        SecondaryTabRow(
//            selectedTabIndex = pagerState.currentPage,
//            containerColor = Color.Transparent,
//            contentColor = PortfolioTheme.colors.primaryText,
//            indicator = {},
//            divider = {}
//        ) {
//            Tabs.entries.forEach { tab ->
//                val selected = pagerState.currentPage == tab.ordinal
//                Tab(
//                    selected = selected,
//                    onClick = {
//                        scope.launch {
//                            pagerState.animateScrollToPage(tab.ordinal)
//                        }
//                    },
//                    modifier = Modifier
////                                .clip(RoundedCornerShape(100))
//                        .background(if (selected) Color(0xFF3F3F4E) else Color.Transparent)
//                        .padding(vertical = 8.dp),
//                    text = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                imageVector = when (tab) {
//                                    Tabs.Projects -> Icons.Rounded.Code
//                                    Tabs.AboutMe -> Icons.Rounded.Person
//                                },
//                                contentDescription = null,
//                                modifier = Modifier.size(16.dp),
//                                tint = if (selected) Color.White else PortfolioTheme.colors.secondaryText
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = tab.title,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Medium,
//                                color = if (selected) Color.White else PortfolioTheme.colors.secondaryText
//                            )
//                        }
//                    }
//                )
//            }
//        }
//    }


    // About me section
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .verticalScroll(rememberScrollState())
//            .padding(horizontal = if (isWideScreen) 48.dp else 20.dp)
//            .padding(top = 100.dp, bottom = 100.dp)
//    ) {
//        PathSectionHeader("About me")
//        AboutSection()
//        Spacer(modifier = Modifier.height(40.dp))
//
//        PathSectionHeader("Work")
//        ExperienceAndSkillsSection(
//            isWideScreen = isWideScreen
//        )
//        Spacer(modifier = Modifier.height(40.dp))
//
//        PathSectionHeader("Education")
//        EducationSection()
//        Spacer(modifier = Modifier.height(40.dp))
//
//        PathSectionHeader("Contacts")
//        ContactSection()
//    }
}