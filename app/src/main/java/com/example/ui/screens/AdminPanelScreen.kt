package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.VfxGlassCard
import com.example.ui.components.VfxGlowingButton
import com.example.ui.theme.CreditGold
import com.example.ui.theme.DeepVfxBackground
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SecondaryVfxBackground
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.MainNavigationTab
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun AdminPanelScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsState()

    var creditTopupAmount by remember { mutableStateOf("1000") }
    var actionMessage by remember { mutableStateOf<String?>(null) }

    // Provider toggles
    var geminiEnabled by remember { mutableStateOf(true) }
    var runwayEnabled by remember { mutableStateOf(true) }
    var lumaEnabled by remember { mutableStateOf(true) }
    var elevenLabsEnabled by remember { mutableStateOf(true) }
    var supabaseSyncEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Banner
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CreditGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEXTGEN AI ROOT ADMIN CONSOLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = CreditGold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SecondaryVfxBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreditGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.navigateTo(MainNavigationTab.DASHBOARD) }
                ) {
                    Text(
                        text = "Exit to User View",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Credit Granting Control Card
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.5f) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚡ Manual Credit Allocation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Inject credits directly into director accounts for testing or VIP grants.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = creditTopupAmount,
                            onValueChange = { creditTopupAmount = it },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_credit_input"),
                            shape = RoundedCornerShape(10.dp),
                            label = { Text("Amount ⚡", color = TextDim, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CreditGold,
                                unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedContainerColor = DeepVfxBackground
                            )
                        )

                        Button(
                            onClick = {
                                val amt = creditTopupAmount.toIntOrNull() ?: 500
                                viewModel.grantAdminCredits(amt)
                                actionMessage = "Successfully credited $amt ⚡ to account!"
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    actionMessage = null
                                }, 3000)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CreditGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_grant_button")
                        ) {
                            Text("Grant Credits", color = DeepVfxBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    if (actionMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = actionMessage!!,
                            fontSize = 11.sp,
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Provider Adapter Health & Toggles
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.4f) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔌 AI Cloud Service Providers & Adapters",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Control routing between Gemini, video rendering nodes, and audio endpoints.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    ProviderToggleRow(
                        name = "Google Gemini AI (Director Engine)",
                        latency = "45ms",
                        status = "ONLINE (Tier 1)",
                        checked = geminiEnabled,
                        onCheckedChange = { geminiEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ProviderToggleRow(
                        name = "Runway & Luma Video Nodes",
                        latency = "120ms",
                        status = "ONLINE (Cluster A)",
                        checked = runwayEnabled,
                        onCheckedChange = { runwayEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ProviderToggleRow(
                        name = "ElevenLabs & Neural Audio TTS",
                        latency = "60ms",
                        status = "ONLINE (Bilingual)",
                        checked = elevenLabsEnabled,
                        onCheckedChange = { elevenLabsEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    ProviderToggleRow(
                        name = "Supabase Cloud Database & Storage Sync",
                        latency = "28ms",
                        status = "CONNECTED (Postgres RLS)",
                        checked = supabaseSyncEnabled,
                        onCheckedChange = { supabaseSyncEnabled = it }
                    )
                }
            }
        }

        // System Telemetry & Cluster Monitor
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.35f) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📊 System Telemetry & Active Jobs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "All Systems Nominal",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryTile(label = "Active Jobs", value = "0", modifier = Modifier.weight(1f))
                        TelemetryTile(label = "API Success Rate", value = "99.8%", modifier = Modifier.weight(1f))
                        TelemetryTile(label = "GPU Nodes", value = "16 Online", modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderToggleRow(
    name: String,
    latency: String,
    status: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DeepVfxBackground,
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(text = "$status • Latency: $latency", fontSize = 10.sp, color = NeonCyan)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = ElectricBlue,
                    uncheckedTrackColor = SecondaryVfxBackground
                )
            )
        }
    }
}

@Composable
private fun TelemetryTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DeepVfxBackground,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = label, fontSize = 9.sp, color = TextMuted)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite, fontFamily = FontFamily.Monospace)
        }
    }
}
