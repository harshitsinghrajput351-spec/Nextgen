package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
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
import androidx.compose.ui.geometry.Offset
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
import kotlin.math.sin

@Composable
fun VoiceMusicStudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf("VOICE") } // "VOICE" or "MUSIC"

    val voiceText by viewModel.voiceText.collectAsState()
    val voiceStyle by viewModel.voiceVoiceStyle.collectAsState()
    val isVoiceGenerating by viewModel.isVoiceGenerating.collectAsState()
    val isVoicePlaying by viewModel.isVoicePlaying.collectAsState()

    val musicPrompt by viewModel.musicPrompt.collectAsState()
    val musicMood by viewModel.musicMood.collectAsState()
    val isMusicGenerating by viewModel.isMusicGenerating.collectAsState()
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsState()

    val voiceStyles = listOf(
        "Hindi Emotive Narrator",
        "English Global Cinema Voice",
        "Dramatic Movie Trailer Baritone",
        "Inspirational Youth Hero",
        "Intimate Whisper Tone",
        "Corporate Executive Pitch"
    )

    val musicMoods = listOf(
        "Inspiring & Triumphant",
        "Epic Bollywood Orchestral",
        "Cyberpunk Electronic Synth",
        "Tense Suspense Brass",
        "Lofi Chill Beats",
        "Ambient Acoustic Strings"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sub-tabs switcher
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryVfxBackground, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (activeSubTab == "VOICE") ElectricBlue else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { activeSubTab = "VOICE" }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = if (activeSubTab == "VOICE") TextWhite else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Voice Studio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (activeSubTab == "VOICE") TextWhite else TextMuted
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (activeSubTab == "MUSIC") ElectricBlue else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { activeSubTab = "MUSIC" }
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = if (activeSubTab == "MUSIC") TextWhite else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Music Studio",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (activeSubTab == "MUSIC") TextWhite else TextMuted
                        )
                    }
                }
            }
        }

        if (activeSubTab == "VOICE") {
            // Voice Studio Content
            item {
                VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.4f) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎙️ Neural Multilingual Voiceover",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Synthesize hyper-expressive character dialogue and narrations in Hindi and English with natural pauses.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = voiceText,
                            onValueChange = { viewModel.voiceText.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("voice_text_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                focusedContainerColor = DeepVfxBackground.copy(alpha = 0.7f),
                                unfocusedContainerColor = DeepVfxBackground.copy(alpha = 0.5f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            placeholder = { Text("Enter script or dialogue...", color = TextDim, fontSize = 13.sp) },
                            minLines = 3,
                            maxLines = 5
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Voice Persona & Timbre",
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
                            voiceStyles.forEach { style ->
                                val isSel = style == voiceStyle
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSel) ElectricBlue else DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) NeonCyan else ElectricBlue.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.voiceVoiceStyle.value = style }
                                ) {
                                    Text(
                                        text = style,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) TextWhite else TextMuted,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        VfxGlowingButton(
                            text = "SYNTHESIZE VOICEOVER",
                            onClick = { viewModel.generateVoiceover() },
                            isLoading = isVoiceGenerating,
                            creditCost = 8,
                            testTag = "generate_voice_button",
                            icon = { Icon(Icons.Default.Mic, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp)) }
                        )
                    }
                }
            }

            // Voice Player & Animated Waveform
            item {
                VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.5f) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔊 Vocal Master Track: $voiceStyle",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SecondaryVfxBackground
                            ) {
                                Text(
                                    text = if (isVoicePlaying) "PLAYING" else "READY",
                                    fontSize = 10.sp,
                                    color = if (isVoicePlaying) NeonGreen else NeonCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Waveform Visualizer
                        AudioWaveformVisualizer(
                            isPlaying = isVoicePlaying,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = ElectricBlue,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModel.isVoicePlaying.value = !viewModel.isVoicePlaying.value
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = TextWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Music Studio Content
            item {
                VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.4f) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎵 AI Symphony & Cinematic Score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Compose full-length orchestral soundtracks, cinematic trailer cues, and electronic beats.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = musicPrompt,
                            onValueChange = { viewModel.musicPrompt.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("music_prompt_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = ElectricBlue.copy(alpha = 0.4f),
                                focusedContainerColor = DeepVfxBackground.copy(alpha = 0.7f),
                                unfocusedContainerColor = DeepVfxBackground.copy(alpha = 0.5f),
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            placeholder = { Text("Describe the music mood and instruments...", color = TextDim, fontSize = 13.sp) },
                            minLines = 2,
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Mood & Instrumentation",
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
                            musicMoods.forEach { mood ->
                                val isSel = mood == musicMood
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSel) ElectricBlue else DeepVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) NeonCyan else ElectricBlue.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { viewModel.musicMood.value = mood }
                                ) {
                                    Text(
                                        text = mood,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) TextWhite else TextMuted,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        VfxGlowingButton(
                            text = "COMPOSE CINEMATIC MUSIC",
                            onClick = { viewModel.generateMusic() },
                            isLoading = isMusicGenerating,
                            creditCost = 15,
                            testTag = "generate_music_button",
                            icon = { Icon(Icons.Default.Audiotrack, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp)) }
                        )
                    }
                }
            }

            // Music Player Card
            item {
                VfxGlassCard(modifier = Modifier.fillMaxWidth(), borderGlowAlpha = 0.5f) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎼 Master Soundtrack: $musicMood",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SecondaryVfxBackground
                            ) {
                                Text(
                                    text = if (isMusicPlaying) "PLAYING" else "PAUSED",
                                    fontSize = 10.sp,
                                    color = if (isMusicPlaying) NeonGreen else CreditGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        AudioWaveformVisualizer(
                            isPlaying = isMusicPlaying,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = ElectricBlue,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.toggleMusicPlayback() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isMusicPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = TextWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AudioWaveformVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DeepVfxBackground.copy(alpha = 0.8f))
            .border(1.dp, ElectricBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val barCount = 36
            val barWidth = w / (barCount * 1.5f)

            for (i in 0 until barCount) {
                val x = i * (w / barCount) + barWidth / 2
                val barHeight = if (isPlaying) {
                    val wave = (sin(i * 0.4f + phase) * 0.5f + 0.5f)
                    (wave * (h * 0.75f)).coerceAtLeast(4f)
                } else {
                    (h * 0.15f)
                }

                drawLine(
                    brush = Brush.verticalGradient(listOf(NeonCyan, ElectricBlue)),
                    start = Offset(x, (h - barHeight) / 2),
                    end = Offset(x, (h + barHeight) / 2),
                    strokeWidth = barWidth
                )
            }
        }
    }
}
