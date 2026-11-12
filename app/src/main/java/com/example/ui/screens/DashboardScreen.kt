package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.components.VfxGlassCard
import com.example.ui.theme.CreditGold
import com.example.ui.theme.DeepVfxBackground
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SecondaryVfxBackground
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.MainNavigationTab
import com.example.ui.viewmodel.StudioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()
    val transactions by viewModel.allTransactions.collectAsState()
    val generations by viewModel.allGenerations.collectAsState()
    val projects by viewModel.allProjects.collectAsState()

    var showTopupToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.5f) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(ElectricBlue, NeonCyan))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Avatar",
                            tint = TextWhite,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile?.displayName ?: "Director",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = CreditGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CreditGold)
                            ) {
                                Text(
                                    text = "PRO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CreditGold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = profile?.email ?: "director@nextgen.ai",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = profile?.userLevel ?: "Studio Executive",
                            fontSize = 11.sp,
                            color = NeonCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SecondaryVfxBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { viewModel.navigateTo(MainNavigationTab.ADMIN) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin", fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Live Credit Wallet
        item {
            VfxGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlowAlpha = 0.7f
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Studio Credit Balance",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = profile?.subscriptionPlan ?: "Pro Studio Ultra",
                            fontSize = 11.sp,
                            color = CreditGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${profile?.creditsBalance ?: 0}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Credits",
                                fontSize = 14.sp,
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.grantAdminCredits(500)
                                showTopupToast = true
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    showTopupToast = false
                                }, 2500)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("topup_credits_button")
                        ) {
                            Text("+500 Topup", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    if (showTopupToast) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✓ 500 Credits added to your studio wallet!",
                            fontSize = 11.sp,
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Studio Stats Metric Tiles
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Videos Created",
                    count = "${profile?.totalVideosGenerated ?: 8}",
                    subtitle = "1080p / 4K Movies",
                    icon = Icons.Default.Movie,
                    color = ElectricBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Images Rendered",
                    count = "${profile?.totalImagesGenerated ?: 32}",
                    subtitle = "DSLR / Anime",
                    icon = Icons.Default.PhotoCamera,
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Subscription Plans
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.4f) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💎 Studio Creator Tiers",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    TierRow(
                        name = "Pro Cinema Ultra (Current)",
                        credits = "2,500 Credits / mo",
                        features = "4K Long Video, Multilingual Hindi/Eng, High-res Audio",
                        isCurrent = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TierRow(
                        name = "Enterprise Production Studio",
                        credits = "10,000 Credits / mo",
                        features = "Unlimited Scene Breakdown, Priority GPU cluster",
                        isCurrent = false
                    )
                }
            }
        }

        // Transaction History List
        item {
            Text(
                text = "⚡ Recent Credit Transactions",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (transactions.isEmpty()) {
            item {
                Text("No transactions yet.", fontSize = 11.sp, color = TextDim)
            }
        } else {
            items(transactions.take(6)) { tx ->
                VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.2f) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.description,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhite
                            )
                            Text(
                                text = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(tx.timestamp)),
                                fontSize = 10.sp,
                                color = TextDim
                            )
                        }

                        Text(
                            text = if (tx.amount > 0) "+${tx.amount} ⚡" else "${tx.amount} ⚡",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tx.amount > 0) NeonGreen else CreditGold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    count: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    VfxGlassCard(modifier = modifier, borderGlowAlpha = 0.35f) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = count, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite, fontFamily = FontFamily.Monospace)
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(text = subtitle, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
private fun TierRow(
    name: String,
    credits: String,
    features: String,
    isCurrent: Boolean
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isCurrent) ElectricBlue.copy(alpha = 0.2f) else DeepVfxBackground,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCurrent) NeonCyan else ElectricBlue.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = credits, fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold)
                Text(text = features, fontSize = 10.sp, color = TextMuted)
            }
            if (isCurrent) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeonGreen.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen)
                ) {
                    Text(
                        text = "ACTIVE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
