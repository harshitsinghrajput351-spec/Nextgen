package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CinematicStyle
import com.example.data.model.Language
import com.example.data.model.VideoDuration
import com.example.data.model.VideoSceneEntity
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
import com.example.ui.theme.VfxBorderGlow
import com.example.ui.theme.VfxCardElevated
import com.example.ui.theme.VfxCardSurface
import com.example.ui.theme.VfxGlowCyan
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun MovieStudioScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val prompt by viewModel.videoPrompt.collectAsState()
    val duration by viewModel.selectedDuration.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val style by viewModel.selectedStyle.collectAsState()
    val isGenerating by viewModel.isVideoGenerating.collectAsState()
    val stages by viewModel.generationStages.collectAsState()
    val activeProject by viewModel.activeProject.collectAsState()
    val scenes by viewModel.activeScenes.collectAsState()
    val timelineState by viewModel.timelineState.collectAsState()

    var showExportDialog by remember { mutableStateOf(false) }
    var selectedSceneForDetail by remember { mutableStateOf<VideoSceneEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Badge ---
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
                        text = "AI LONG VIDEO & MOVIE STUDIO",
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
                        text = "MULTI-SCENE ENGINE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VfxGlowCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // --- Prompt Creation Console ---
        item {
            VfxGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderGlowAlpha = 0.4f
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎬 Direct Cinematic Movie",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "Enter a story idea. The AI Director generates characters, script, scenes, Hindi/English dialogues, and video timeline.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { viewModel.videoPrompt.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("movie_prompt_input"),
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
                            Text(
                                "e.g. Create a 20-minute Hindi cinematic movie about a poor boy who becomes successful in AI technology.",
                                fontSize = 13.sp,
                                color = TextDim
                            )
                        },
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Duration Selector Pill Row
                    Text(
                        text = "Duration Target",
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
                        VideoDuration.values().forEach { d ->
                            val isSelected = d == duration
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) ElectricBlue else DeepVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) NeonCyan else ElectricBlue.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.selectedDuration.value = d }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = d.label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextWhite else TextMuted
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${d.creditCost}⚡",
                                        fontSize = 10.sp,
                                        color = if (isSelected) CreditGold else TextDim
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Language & Style Grid
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Language Dropdown Selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Dialogue Language",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SecondaryVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        val next = when (language) {
                                            Language.BILINGUAL -> Language.HINDI
                                            Language.HINDI -> Language.ENGLISH
                                            Language.ENGLISH -> Language.BILINGUAL
                                        }
                                        viewModel.selectedLanguage.value = next
                                    }
                            ) {
                                Text(
                                    text = "🌐 ${language.displayName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Style Selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cinematic Preset",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SecondaryVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        val all = CinematicStyle.values()
                                        val nextIdx = (all.indexOf(style) + 1) % all.size
                                        viewModel.selectedStyle.value = all[nextIdx]
                                    }
                            ) {
                                Text(
                                    text = "🎥 ${style.label}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Generation Action Button
                    VfxGlowingButton(
                        text = "PRODUCE MOVIE PROJECT",
                        onClick = { viewModel.startMovieGeneration() },
                        isLoading = isGenerating,
                        creditCost = duration.creditCost,
                        testTag = "produce_movie_button",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

        // --- Live Generation Stepper (If Generating) ---
        if (isGenerating && stages.isNotEmpty()) {
            item {
                VfxGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGlowAlpha = 0.7f
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚡ AI Director Synthesis in Progress",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = NeonCyan,
                                strokeWidth = 2.dp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        stages.forEach { stage ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (stage.isCompleted) Icons.Default.CheckCircle else Icons.Default.FiberManualRecord,
                                    contentDescription = null,
                                    tint = when {
                                        stage.isCompleted -> NeonGreen
                                        stage.isRunning -> NeonCyan
                                        else -> TextDim
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${stage.id}. ${stage.title}",
                                        fontSize = 12.sp,
                                        fontWeight = if (stage.isRunning) FontWeight.Bold else FontWeight.Medium,
                                        color = if (stage.isRunning) TextWhite else if (stage.isCompleted) TextMuted else TextDim
                                    )
                                    if (stage.isRunning) {
                                        Text(
                                            text = stage.description,
                                            fontSize = 10.sp,
                                            color = NeonCyan
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Active Project Monitor & Interactive Video Player ---
        if (activeProject != null && scenes.isNotEmpty()) {
            val currSceneIdx = timelineState.activeSceneIndex.coerceIn(0, scenes.size - 1)
            val currentScene = scenes[currSceneIdx]

            item {
                VfxGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGlowAlpha = 0.5f
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Title bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeProject!!.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${activeProject!!.durationLabel} • ${activeProject!!.resolution} • ${scenes.size} Scenes",
                                    fontSize = 11.sp,
                                    color = NeonCyan
                                )
                            }

                            Row {
                                IconButton(onClick = { showExportDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Export Video",
                                        tint = NeonCyan
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleFullscreen() }) {
                                    Icon(
                                        imageVector = if (timelineState.isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                        contentDescription = "Fullscreen",
                                        tint = TextWhite
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // --- Futuristic Cinema Monitor / Canvas Display ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF030712))
                                .border(1.dp, Brush.linearGradient(listOf(NeonCyan, ElectricBlue)), RoundedCornerShape(12.dp))
                        ) {
                            // Cinematic Atmosphere Layer
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                ElectricBlue.copy(alpha = 0.15f),
                                                Color.Transparent,
                                                DeepVfxBackground.copy(alpha = 0.9f)
                                            )
                                        )
                                    )
                            )

                            // HUD Elements: REC indicator, FPS, Resolution
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (timelineState.isPlaying) NeonRed else TextDim)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (timelineState.isPlaying) "● LIVE AI RENDER" else "STILL PREVIEW",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (timelineState.isPlaying) NeonRed else TextMuted,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Surface(
                                    color = DeepVfxBackground.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = activeProject!!.resolution,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            // Center scene visualization details
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = DeepVfxBackground.copy(alpha = 0.8f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "📍 ${currentScene.location}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🎥 ${currentScene.cameraAngle}",
                                    fontSize = 11.sp,
                                    color = NeonCyan,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "👤 ${currentScene.characterName}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }

                            // Subtitles at the bottom of the monitor
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.75f))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (timelineState.showHindiSubtitles) {
                                    Text(
                                        text = currentScene.hindiDialogueText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CreditGold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Text(
                                    text = "\"${currentScene.dialogueText}\"",
                                    fontSize = 11.sp,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // --- Timeline Scrubber & Controls ---
                        val totalDurationSec = activeProject!!.durationSeconds.coerceAtLeast(1)
                        val progressFraction = (timelineState.currentPositionSec.toFloat() / totalDurationSec).coerceIn(0f, 1f)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatTime(timelineState.currentPositionSec),
                                fontSize = 11.sp,
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Scene ${currentScene.sceneNumber}/${scenes.size}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = formatTime(totalDurationSec),
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Slider(
                            value = progressFraction,
                            onValueChange = { frac ->
                                val targetSec = (frac * totalDurationSec).toInt()
                                val targetSceneIdx = scenes.indexOfFirst { targetSec in it.timeStartSec..it.timeEndSec }
                                viewModel.timelineState.value = timelineState.copy(
                                    currentPositionSec = targetSec,
                                    activeSceneIndex = if (targetSceneIdx != -1) targetSceneIdx else timelineState.activeSceneIndex
                                )
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = NeonCyan,
                                activeTrackColor = ElectricBlue,
                                inactiveTrackColor = SecondaryVfxBackground
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Transport Controls: Play/Pause, Scene Prev/Next, Subtitles, Mute
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    val prev = (currSceneIdx - 1).coerceAtLeast(0)
                                    viewModel.seekToScene(prev)
                                }
                            ) {
                                Icon(Icons.Default.FastRewind, contentDescription = "Prev Scene", tint = TextWhite)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Glowing Play/Pause FAB
                            Surface(
                                shape = CircleShape,
                                color = ElectricBlue,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.toggleTimelinePlay() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (timelineState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (timelineState.isPlaying) "Pause" else "Play",
                                        tint = TextWhite,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {
                                    val next = (currSceneIdx + 1).coerceAtMost(scenes.size - 1)
                                    viewModel.seekToScene(next)
                                }
                            ) {
                                Icon(Icons.Default.FastForward, contentDescription = "Next Scene", tint = TextWhite)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            IconButton(onClick = { viewModel.toggleSubtitles() }) {
                                Icon(
                                    imageVector = Icons.Default.ClosedCaption,
                                    contentDescription = "Toggle Subtitles",
                                    tint = if (timelineState.showHindiSubtitles) NeonCyan else TextDim
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.timelineState.value = timelineState.copy(isMuted = !timelineState.isMuted)
                                }
                            ) {
                                Icon(
                                    imageVector = if (timelineState.isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                                    contentDescription = "Mute Audio",
                                    tint = if (timelineState.isMuted) NeonRed else TextWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // --- Multi-Scene Strip / Timeline Blocks ---
                        Text(
                            text = "Scene Timeline Blocks (Tap to Jump)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            scenes.forEachIndexed { index, scene ->
                                val isCurrent = index == currSceneIdx
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isCurrent) ElectricBlue.copy(alpha = 0.4f) else SecondaryVfxBackground,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isCurrent) NeonCyan else ElectricBlue.copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            viewModel.seekToScene(index)
                                            selectedSceneForDetail = scene
                                        }
                                        .width(130.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Act ${index + 1}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) NeonCyan else TextMuted
                                            )
                                            Text(
                                                text = formatTime(scene.timeStartSec),
                                                fontSize = 9.sp,
                                                color = TextDim,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = scene.location,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = scene.characterName,
                                            fontSize = 9.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- Dedicated Scene & Character Inspector Card ---
            item {
                VfxGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderGlowAlpha = 0.35f
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎭 Scene Inspector: Scene ${currentScene.sceneNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SecondaryVfxBackground
                            ) {
                                Text(
                                    text = currentScene.bgmMood.take(24),
                                    fontSize = 10.sp,
                                    color = CreditGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Visual Directive & Setting:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Text(
                            text = currentScene.visualDescription,
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )

                        Text(
                            text = "Character Dialogue (Hindi / English):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DeepVfxBackground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "🗣️ ${currentScene.hindiDialogueText}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CreditGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "🇬🇧 \"${currentScene.dialogueText}\"",
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Camera & Lighting Directive:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Text(
                            text = currentScene.cameraAngle,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }

    // --- Export Video Dialog ---
    if (showExportDialog && activeProject != null) {
        var exportFormat by remember { mutableStateOf("1080p FHD (Recommended)") }
        var isExporting by remember { mutableStateOf(false) }
        var exportProgress by remember { mutableStateOf(0f) }
        var exportFinished by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text(
                    text = "Export Movie: ${activeProject!!.title}",
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Stitch all ${scenes.size} scenes into a single cinematic MP4 video file with synchronized bilingual audio tracks and burned-in subtitles.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isExporting && !exportFinished) {
                        Text(
                            text = "Select Master Quality:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        listOf("4K UHD Cinema (Pro)", "1080p FHD (Recommended)", "720p Mobile Quick").forEach { fmt ->
                            val isSel = exportFormat == fmt
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) ElectricBlue.copy(alpha = 0.3f) else DeepVfxBackground,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) NeonCyan else Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { exportFormat = fmt }
                            ) {
                                Text(
                                    text = fmt,
                                    fontSize = 12.sp,
                                    color = if (isSel) TextWhite else TextMuted,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    } else if (isExporting) {
                        LinearProgressIndicator(
                            progress = { exportProgress },
                            color = NeonCyan,
                            trackColor = SecondaryVfxBackground,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rendering frames & FFmpeg stitching... ${(exportProgress * 100).toInt()}%",
                            fontSize = 11.sp,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Movie Export Ready! Master package compiled.",
                                fontSize = 12.sp,
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (!exportFinished) {
                    TextButton(
                        onClick = {
                            isExporting = true
                            exportProgress = 0.35f
                            // Simulate quick export progression
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                exportProgress = 0.85f
                            }, 800)
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                exportProgress = 1f
                                isExporting = false
                                exportFinished = true
                            }, 1600)
                        },
                        enabled = !isExporting
                    ) {
                        Text("Start Export", color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                } else {
                    TextButton(onClick = { showExportDialog = false }) {
                        Text("Done", color = NeonGreen, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = TextMuted)
                }
            },
            containerColor = VfxCardElevated
        )
    }
}

private fun formatTime(sec: Int): String {
    val m = sec / 60
    val s = sec % 60
    return String.format("%02d:%02d", m, s)
}
