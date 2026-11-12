package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.ui.theme.VfxBorderGlow
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun ImageStudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val prompt by viewModel.imagePrompt.collectAsState()
    val stylePreset by viewModel.imageStylePreset.collectAsState()
    val aspectRatio by viewModel.imageAspectRatio.collectAsState()
    val isGenerating by viewModel.isImageGenerating.collectAsState()
    val lastSummary by viewModel.lastGeneratedImageSummary.collectAsState()

    val presets = listOf(
        "DSLR Ultra-Real",
        "Anime Masterpiece",
        "3D Cartoon CGI",
        "Cyberpunk Neon",
        "Brand Logo Vector",
        "YouTube Thumbnail",
        "AI Studio Portrait",
        "Cinematic Matte Paint"
    )

    val aspectRatios = listOf("16:9 Cinema", "1:1 Square", "9:16 Vertical")

    var selectedImageModal by remember { mutableStateOf<String?>(null) }

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
                        text = "AI IMAGE & PHOTO STUDIO",
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
                        text = "8K NEURAL RENDER",
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
            VfxGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlowAlpha = 0.4f
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎨 Neural Image Synthesis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Generate photorealistic imagery, logos, characters, and thumbnails with fine lighting control.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { viewModel.imagePrompt.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("image_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                            focusedContainerColor = DeepVfxBackground.copy(alpha = 0.7f),
                            unfocusedContainerColor = DeepVfxBackground.copy(alpha = 0.5f),
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        placeholder = {
                            Text("Describe your image in vivid detail...", color = TextDim, fontSize = 13.sp)
                        },
                        minLines = 2,
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Style Preset",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.forEach { preset ->
                            val isSelected = preset == stylePreset
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) ElectricBlue else DeepVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) NeonCyan else ElectricBlue.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.imageStylePreset.value = preset }
                            ) {
                                Text(
                                    text = preset,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TextWhite else TextMuted,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Aspect Ratio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        aspectRatios.forEach { ratio ->
                            val isSel = ratio == aspectRatio
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) ElectricBlue.copy(alpha = 0.4f) else SecondaryVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) NeonCyan else Color.Transparent
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.imageAspectRatio.value = ratio }
                            ) {
                                Text(
                                    text = ratio,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) TextWhite else TextMuted,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    VfxGlowingButton(
                        text = "GENERATE IMAGE",
                        onClick = { viewModel.generateImage() },
                        isLoading = isGenerating,
                        creditCost = 5,
                        testTag = "generate_image_button",
                        icon = {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp))
                        }
                    )
                }
            }
        }

        // Active Masterpiece Showcase
        item {
            VfxGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlowAlpha = 0.5f
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🖼️ Featured Master Render: Cyber Command HQ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SecondaryVfxBackground
                        ) {
                            Text(
                                text = "16:9 • 8K UHD",
                                fontSize = 10.sp,
                                color = NeonCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nextgen_hero_banner_1794453291388),
                            contentDescription = "Generated Masterpiece",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Glow border overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, DeepVfxBackground.copy(alpha = 0.6f))
                                    )
                                )
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = DeepVfxBackground.copy(alpha = 0.8f)
                        ) {
                            Text(
                                text = "Neo-Genesis Studio 2099",
                                fontSize = 11.sp,
                                color = TextWhite,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lastSummary ?: "Neural rendering pipeline ready for new prompts.",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "⚡ Instant Upscale Ready",
                            fontSize = 11.sp,
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
