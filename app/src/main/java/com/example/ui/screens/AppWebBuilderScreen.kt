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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.components.VfxGlowingButton
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
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun AppWebBuilderScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val prompt by viewModel.appPrompt.collectAsState()
    val targetPlatform by viewModel.appTargetPlatform.collectAsState()
    val isGenerating by viewModel.isAppGenerating.collectAsState()
    val generatedCode by viewModel.generatedAppCode.collectAsState()
    val cartCount by viewModel.appCartCount.collectAsState()

    var activeViewMode by remember { mutableStateOf("PREVIEW") } // "PREVIEW" or "CODE"
    var exportToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                            .background(NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI APP & WEBSITE ARCHITECT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = NeonCyan
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SecondaryVfxBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "LIVE SANDBOX",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VfxGlowCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Generator Card
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.4f) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💻 Prompt to Full Application",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Enter feature specifications. AI synthesizes complete Kotlin Jetpack Compose or React code with working UI components.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { viewModel.appPrompt.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("app_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                            focusedContainerColor = DeepVfxBackground.copy(alpha = 0.7f),
                            unfocusedContainerColor = DeepVfxBackground.copy(alpha = 0.5f),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        minLines = 2,
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Target Platform & Framework",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val platforms = listOf("Android Jetpack Compose", "React + Tailwind Web")
                        platforms.forEach { p ->
                            val isSel = p == targetPlatform
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) ElectricBlue else DeepVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) NeonCyan else ElectricBlue.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.appTargetPlatform.value = p }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (p.contains("Android")) Icons.Default.Android else Icons.Default.Language,
                                        contentDescription = null,
                                        tint = if (isSel) TextWhite else TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (p.contains("Android")) "Android App" else "Web App",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) TextWhite else TextMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    VfxGlowingButton(
                        text = "BUILD & COMPILE APPLICATION",
                        onClick = { viewModel.generateAppOrWebsite() },
                        isLoading = isGenerating,
                        creditCost = 25,
                        testTag = "build_app_button",
                        icon = { Icon(Icons.Default.Code, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }

        // Live Workspace Card (Preview vs Code Toggle)
        item {
            VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.5f) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .background(SecondaryVfxBackground, RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (activeViewMode == "PREVIEW") ElectricBlue else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { activeViewMode = "PREVIEW" }
                            ) {
                                Text(
                                    text = "📱 Live Preview",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeViewMode == "PREVIEW") TextWhite else TextMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (activeViewMode == "CODE") ElectricBlue else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { activeViewMode = "CODE" }
                            ) {
                                Text(
                                    text = "📄 Code Inspector",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeViewMode == "CODE") TextWhite else TextMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        // Export ZIP button
                        IconButton(
                            onClick = {
                                exportToast = true
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    exportToast = false
                                }, 2500)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Export ZIP",
                                tint = NeonCyan
                            )
                        }
                    }

                    if (exportToast) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NeonGreen.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Project ZIP Package generated! Includes Gradle / npm configs.",
                                    fontSize = 11.sp,
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (activeViewMode == "PREVIEW") {
                        // Interactive Sandboxed Shopping App Simulation
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF070F20),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                // Mini App Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "⚡ Apex Commerce",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhite
                                    )

                                    BadgedBox(
                                        badge = {
                                            Badge(containerColor = NeonCyan) {
                                                Text("$cartCount", color = DeepVfxBackground, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = "Cart",
                                            tint = TextWhite,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Featured Product Item
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Neural Quantum Glasses v4",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Real-time AI HUD • 4K OLED",
                                                fontSize = 10.sp,
                                                color = TextMuted
                                            )
                                            Text(
                                                text = "$499.00 USD",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = CreditGold
                                            )
                                        }

                                        Button(
                                            onClick = { viewModel.appCartCount.value = cartCount + 1 },
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text("+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Second Product Item
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Cyber Clapper Director Rig",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Wireless SMPTE Timecode Sync",
                                                fontSize = 10.sp,
                                                color = TextMuted
                                            )
                                            Text(
                                                text = "$189.00 USD",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = CreditGold
                                            )
                                        }

                                        Button(
                                            onClick = { viewModel.appCartCount.value = cartCount + 1 },
                                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text("+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Code Inspector View
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF030712),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = generatedCode ?: "// Click 'BUILD & COMPILE APPLICATION' above to generate source code.",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = NeonCyan,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
