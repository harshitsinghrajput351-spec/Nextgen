package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CreditBalanceChip
import com.example.ui.components.VfxParticleBackground
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AppWebBuilderScreen
import com.example.ui.screens.CreativeToolsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ImageStudioScreen
import com.example.ui.screens.MovieStudioScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.VoiceMusicStudioScreen
import com.example.ui.theme.CreditGold
import com.example.ui.theme.DeepVfxBackground
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NextGenTheme
import com.example.ui.theme.SecondaryVfxBackground
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VfxCardSurface
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.MainNavigationTab
import com.example.ui.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: StudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextGenTheme {
                NextGenMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NextGenMainApp(viewModel: StudioViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    // Back handling: pop back to Movie Studio if on sub-screen
    BackHandler(enabled = currentTab != MainNavigationTab.LONG_VIDEO_STUDIO) {
        viewModel.navigateTo(MainNavigationTab.LONG_VIDEO_STUDIO)
    }

    VfxParticleBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                NextGenTopAppBar(
                    credits = profile?.creditsBalance ?: 0,
                    currentTab = currentTab,
                    onTabSelected = { viewModel.navigateTo(it) },
                    onCreditClick = { viewModel.navigateTo(MainNavigationTab.DASHBOARD) },
                    onAdminClick = { viewModel.navigateTo(MainNavigationTab.ADMIN) }
                )
            },
            bottomBar = {
                NextGenBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.navigateTo(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    MainNavigationTab.LONG_VIDEO_STUDIO -> MovieStudioScreen(viewModel = viewModel)
                    MainNavigationTab.IMAGE_STUDIO -> ImageStudioScreen(viewModel = viewModel)
                    MainNavigationTab.VOICE_MUSIC_STUDIO -> VoiceMusicStudioScreen(viewModel = viewModel)
                    MainNavigationTab.APP_WEB_BUILDER -> AppWebBuilderScreen(viewModel = viewModel)
                    MainNavigationTab.CREATIVE_TOOLS -> CreativeToolsScreen(viewModel = viewModel)
                    MainNavigationTab.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                    MainNavigationTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    MainNavigationTab.ADMIN -> AdminPanelScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NextGenTopAppBar(
    credits: Int,
    currentTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit,
    onCreditClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(DeepVfxBackground.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(ElectricBlue.copy(alpha = 0.3f), Color.Transparent)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .padding(top = 8.dp, bottom = 6.dp)
    ) {
        // Upper Title Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(listOf(ElectricBlue, NeonCyan))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "NEXTGEN Logo",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "NEXTGEN AI",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "ULTIMATE AI CREATION STUDIO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                CreditBalanceChip(
                    balance = credits,
                    onClick = onCreditClick
                )

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = CircleShape,
                    color = SecondaryVfxBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onAdminClick)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Panel",
                            tint = if (currentTab == MainNavigationTab.ADMIN) CreditGold else NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Studio Switcher Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StudioQuickTab(
                label = "🎬 Long Video",
                isSelected = currentTab == MainNavigationTab.LONG_VIDEO_STUDIO,
                onClick = { onTabSelected(MainNavigationTab.LONG_VIDEO_STUDIO) }
            )
            StudioQuickTab(
                label = "🖼️ Images",
                isSelected = currentTab == MainNavigationTab.IMAGE_STUDIO,
                onClick = { onTabSelected(MainNavigationTab.IMAGE_STUDIO) }
            )
            StudioQuickTab(
                label = "🎙️ Voice & Music",
                isSelected = currentTab == MainNavigationTab.VOICE_MUSIC_STUDIO,
                onClick = { onTabSelected(MainNavigationTab.VOICE_MUSIC_STUDIO) }
            )
            StudioQuickTab(
                label = "💻 App & Web",
                isSelected = currentTab == MainNavigationTab.APP_WEB_BUILDER,
                onClick = { onTabSelected(MainNavigationTab.APP_WEB_BUILDER) }
            )
            StudioQuickTab(
                label = "✨ Creative Tools",
                isSelected = currentTab == MainNavigationTab.CREATIVE_TOOLS,
                onClick = { onTabSelected(MainNavigationTab.CREATIVE_TOOLS) }
            )
        }
    }
}

@Composable
fun StudioQuickTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) ElectricBlue else SecondaryVfxBackground,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) NeonCyan else ElectricBlue.copy(alpha = 0.25f)
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TextWhite else TextMuted,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun NextGenBottomNavigation(
    currentTab: MainNavigationTab,
    onTabSelected: (MainNavigationTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(NeonCyan.copy(alpha = 0.4f), Color.Transparent)
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ),
        containerColor = DeepVfxBackground.copy(alpha = 0.96f),
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple(MainNavigationTab.LONG_VIDEO_STUDIO, "Cinema", Icons.Default.Movie),
            Triple(MainNavigationTab.IMAGE_STUDIO, "Image", Icons.Default.Image),
            Triple(MainNavigationTab.VOICE_MUSIC_STUDIO, "Audio", Icons.Default.Mic),
            Triple(MainNavigationTab.APP_WEB_BUILDER, "Code", Icons.Default.Code),
            Triple(MainNavigationTab.PROJECTS, "Projects", Icons.Default.Folder),
            Triple(MainNavigationTab.DASHBOARD, "Wallet", Icons.Default.Dashboard)
        )

        navItems.forEach { (tab, label, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TextWhite,
                    selectedTextColor = NeonCyan,
                    indicatorColor = ElectricBlue,
                    unselectedIconColor = TextDim,
                    unselectedTextColor = TextDim
                )
            )
        }
    }
}
