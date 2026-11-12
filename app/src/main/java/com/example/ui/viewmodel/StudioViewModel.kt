package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CinematicStyle
import com.example.data.model.CreditTransactionEntity
import com.example.data.model.GenerationHistoryEntity
import com.example.data.model.GenerationStage
import com.example.data.model.Language
import com.example.data.model.ProjectEntity
import com.example.data.model.StudioType
import com.example.data.model.UserProfileEntity
import com.example.data.model.VideoDuration
import com.example.data.model.VideoSceneEntity
import com.example.data.repository.NextGenRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainNavigationTab {
    LONG_VIDEO_STUDIO,
    IMAGE_STUDIO,
    VOICE_MUSIC_STUDIO,
    APP_WEB_BUILDER,
    CREATIVE_TOOLS,
    PROJECTS,
    DASHBOARD,
    ADMIN
}

data class MovieTimelineState(
    val isPlaying: Boolean = false,
    val currentPositionSec: Int = 0,
    val activeSceneIndex: Int = 0,
    val isMuted: Boolean = false,
    val showHindiSubtitles: Boolean = true,
    val isFullscreen: Boolean = false
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {
    val repository = NextGenRepository(application)

    val allProjects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGenerations: StateFlow<List<GenerationHistoryEntity>> = repository.allGenerations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<CreditTransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current navigation tab
    private val _currentTab = MutableStateFlow(MainNavigationTab.LONG_VIDEO_STUDIO)
    val currentTab: StateFlow<MainNavigationTab> = _currentTab.asStateFlow()

    fun navigateTo(tab: MainNavigationTab) {
        _currentTab.value = tab
    }

    // --- AI Long Video & Movie Studio State ---
    val videoPrompt = MutableStateFlow("Create a 20-minute Hindi cinematic movie about a poor boy who becomes successful in AI technology.")
    val selectedDuration = MutableStateFlow(VideoDuration.MIN_20)
    val selectedLanguage = MutableStateFlow(Language.BILINGUAL)
    val selectedStyle = MutableStateFlow(CinematicStyle.BOLLYWOOD_EPIC)

    private val _isVideoGenerating = MutableStateFlow(false)
    val isVideoGenerating: StateFlow<Boolean> = _isVideoGenerating.asStateFlow()

    private val _generationStages = MutableStateFlow<List<GenerationStage>>(emptyList())
    val generationStages: StateFlow<List<GenerationStage>> = _generationStages.asStateFlow()

    private val _activeProject = MutableStateFlow<ProjectEntity?>(null)
    val activeProject: StateFlow<ProjectEntity?> = _activeProject.asStateFlow()

    private val _activeScenes = MutableStateFlow<List<VideoSceneEntity>>(emptyList())
    val activeScenes: StateFlow<List<VideoSceneEntity>> = _activeScenes.asStateFlow()

    val timelineState = MutableStateFlow(MovieTimelineState())
    private var timelinePlayJob: Job? = null

    // --- AI Image Studio State ---
    val imagePrompt = MutableStateFlow("Cinematic film still of an Indian youth in futuristic Mumbai, neon cyan lighting, 8k DSLR quality")
    val imageStylePreset = MutableStateFlow("DSLR Ultra-Real")
    val imageAspectRatio = MutableStateFlow("16:9 Cinema")
    val isImageGenerating = MutableStateFlow(false)
    val lastGeneratedImageSummary = MutableStateFlow<String?>(null)

    // --- AI Voice & Music Studio State ---
    val voiceText = MutableStateFlow("People say destiny is written on palms. But if you have no hands, where is your destiny? It's inside the fire of your heart.")
    val voiceVoiceStyle = MutableStateFlow("Hindi Emotive Narrator")
    val isVoiceGenerating = MutableStateFlow(false)
    val isVoicePlaying = MutableStateFlow(false)

    val musicPrompt = MutableStateFlow("Grand Bollywood symphonic crescendo with emotional sitar, sweeping violins and modern synth bass")
    val musicMood = MutableStateFlow("Inspiring & Triumphant")
    val isMusicGenerating = MutableStateFlow(false)
    val isMusicPlaying = MutableStateFlow(false)

    // --- AI App & Website Builder State ---
    val appPrompt = MutableStateFlow("Create a professional shopping application with cart, checkout, category search, and dark mode.")
    val appTargetPlatform = MutableStateFlow("Android Jetpack Compose")
    val isAppGenerating = MutableStateFlow(false)
    val generatedAppCode = MutableStateFlow<String?>(null)
    val appCartCount = MutableStateFlow(2)

    init {
        // Automatically load initial featured project if available
        viewModelScope.launch {
            allProjects.collect { projects ->
                if (_activeProject.value == null && projects.isNotEmpty()) {
                    val featured = projects.find { it.id == "proj_featured_movie_01" } ?: projects.first()
                    _activeProject.value = featured
                    _activeScenes.value = repository.getScenesForProject(featured.id)
                }
            }
        }
    }

    // --- Long Video Generation Workflow ---
    fun startMovieGeneration() {
        if (_isVideoGenerating.value) return
        val cost = selectedDuration.value.creditCost
        val currentCredits = userProfile.value?.creditsBalance ?: 0
        if (currentCredits < cost) {
            // Need credits
            return
        }

        viewModelScope.launch {
            _isVideoGenerating.value = true

            val stages = listOf(
                GenerationStage(1, "Deconstructing Prompt & Narrative Arcs", "Analyzing themes, character motivations, and world building...", false, true),
                GenerationStage(2, "Generating Complete Movie Script", "Structuring three-act screenplay with scene descriptions...", false, false),
                GenerationStage(3, "Character Appearance Consistency", "Profiling visual traits and tone anchors for Aarav & Vikram...", false, false),
                GenerationStage(4, "Scene-by-Scene Visual Camera Directives", "Calculating low-angle pans, lens apertures, and cinematic lighting...", false, false),
                GenerationStage(5, "Bilingual Voiceover & Dialogue Synthesis", "Synthesizing Hindi & English emotional vocal tracks...", false, false),
                GenerationStage(6, "Composing Original Orchestral Score", "Scoring emotional strings, tension rhythms, and sound effects...", false, false),
                GenerationStage(7, "Final Video Stitching & 4K Timeline Export", "Stitching scene blocks, subtitle alignment, and audio master...", false, false)
            )
            _generationStages.value = stages

            for (i in stages.indices) {
                delay(800)
                _generationStages.value = _generationStages.value.mapIndexed { idx, stage ->
                    when {
                        idx < i -> stage.copy(isCompleted = true, isRunning = false)
                        idx == i -> stage.copy(isCompleted = false, isRunning = true)
                        else -> stage.copy(isCompleted = false, isRunning = false)
                    }
                }
            }
            delay(900)
            _generationStages.value = _generationStages.value.map { it.copy(isCompleted = true, isRunning = false) }

            // Finalize project creation in repository
            val result = repository.createMovieProject(
                prompt = videoPrompt.value,
                duration = selectedDuration.value,
                language = selectedLanguage.value,
                style = selectedStyle.value
            )

            _activeProject.value = result.first
            _activeScenes.value = result.second
            _isVideoGenerating.value = false
            timelineState.value = MovieTimelineState(currentPositionSec = 0, activeSceneIndex = 0)
        }
    }

    fun selectProject(project: ProjectEntity) {
        viewModelScope.launch {
            _activeProject.value = project
            _activeScenes.value = repository.getScenesForProject(project.id)
            timelineState.value = MovieTimelineState(currentPositionSec = 0, activeSceneIndex = 0)
            _currentTab.value = MainNavigationTab.LONG_VIDEO_STUDIO
        }
    }

    fun toggleTimelinePlay() {
        val current = timelineState.value
        val isNowPlaying = !current.isPlaying
        timelineState.value = current.copy(isPlaying = isNowPlaying)

        if (isNowPlaying) {
            timelinePlayJob?.cancel()
            timelinePlayJob = viewModelScope.launch {
                val scenes = _activeScenes.value
                val totalDuration = _activeProject.value?.durationSeconds ?: 1200
                while (timelineState.value.isPlaying) {
                    delay(1000)
                    val nextSec = timelineState.value.currentPositionSec + 25 // Speed up for rich preview
                    if (nextSec >= totalDuration) {
                        timelineState.value = timelineState.value.copy(
                            isPlaying = false,
                            currentPositionSec = 0,
                            activeSceneIndex = 0
                        )
                        break
                    }
                    val currentSceneIdx = scenes.indexOfFirst { nextSec in it.timeStartSec..it.timeEndSec }
                    timelineState.value = timelineState.value.copy(
                        currentPositionSec = nextSec,
                        activeSceneIndex = if (currentSceneIdx != -1) currentSceneIdx else timelineState.value.activeSceneIndex
                    )
                }
            }
        } else {
            timelinePlayJob?.cancel()
        }
    }

    fun seekToScene(index: Int) {
        val scenes = _activeScenes.value
        if (index in scenes.indices) {
            val scene = scenes[index]
            timelineState.value = timelineState.value.copy(
                currentPositionSec = scene.timeStartSec,
                activeSceneIndex = index
            )
        }
    }

    fun toggleSubtitles() {
        timelineState.value = timelineState.value.copy(
            showHindiSubtitles = !timelineState.value.showHindiSubtitles
        )
    }

    fun toggleFullscreen() {
        timelineState.value = timelineState.value.copy(
            isFullscreen = !timelineState.value.isFullscreen
        )
    }

    // --- Image Generation ---
    fun generateImage() {
        if (isImageGenerating.value) return
        viewModelScope.launch {
            isImageGenerating.value = true
            delay(1500)
            val summary = "Generated [${imageStylePreset.value}] • ${imageAspectRatio.value} • 8K Ultra-Real"
            lastGeneratedImageSummary.value = summary
            repository.recordGeneration(
                studio = "AI Image Studio",
                prompt = imagePrompt.value,
                title = "Cyber Horizon Frame #${(100..999).random()}",
                summary = summary,
                cost = 5
            )
            isImageGenerating.value = false
        }
    }

    // --- Voice & Audio Synthesis ---
    fun generateVoiceover() {
        if (isVoiceGenerating.value) return
        viewModelScope.launch {
            isVoiceGenerating.value = true
            delay(1200)
            isVoiceGenerating.value = false
            isVoicePlaying.value = true
            repository.recordGeneration(
                studio = "AI Voice Studio",
                prompt = voiceText.value.take(60),
                title = "Speech: ${voiceVoiceStyle.value}",
                summary = "Bilingual Neural TTS • 48kHz WAV • Timbre matched",
                cost = 8
            )
            delay(4000)
            isVoicePlaying.value = false
        }
    }

    fun generateMusic() {
        if (isMusicGenerating.value) return
        viewModelScope.launch {
            isMusicGenerating.value = true
            delay(1600)
            isMusicGenerating.value = false
            isMusicPlaying.value = true
            repository.recordGeneration(
                studio = "AI Music Studio",
                prompt = musicPrompt.value,
                title = "Original Score: ${musicMood.value}",
                summary = "Full Orchestration • 320kbps Master • Cinematic Stems",
                cost = 15
            )
        }
    }

    fun toggleMusicPlayback() {
        isMusicPlaying.value = !isMusicPlaying.value
    }

    // --- App & Website Builder ---
    fun generateAppOrWebsite() {
        if (isAppGenerating.value) return
        viewModelScope.launch {
            isAppGenerating.value = true
            delay(1800)
            val isAndroid = appTargetPlatform.value.contains("Android")
            val code = if (isAndroid) {
                """
// Generated Jetpack Compose Application: ApexStoreApp.kt
package com.example.apexstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ApexStoreScreen() {
    var cartItems by remember { mutableStateOf(2) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚡ Apex Modern Commerce") },
                actions = {
                    Badge(badgeContent = { Text("${'$'}cartItems") }) {
                        IconButton(onClick = { /* View Cart */ }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item { HeroPromoBanner() }
            item { ProductGrid(onAddToCart = { cartItems++ }) }
            item { FastCheckoutBar() }
        }
    }
}
                """.trimIndent()
            } else {
                """
// Generated React 18 + Tailwind Component: NextGenStore.tsx
import React, { useState } from 'react';
import { ShoppingCart, Zap, Star } from 'lucide-react';

export default function NextGenStore() {
  const [cart, setCart] = useState(2);
  return (
    <div className="min-h-screen bg-slate-950 text-white font-sans">
      <header className="p-4 border-b border-cyan-500/20 flex justify-between items-center">
        <h1 className="text-xl font-bold bg-gradient-to-r from-blue-500 to-cyan-400 bg-clip-text text-transparent">
          ⚡ APEX COMMERCE
        </h1>
        <button className="px-4 py-2 bg-blue-600 rounded-lg flex items-center gap-2">
          <ShoppingCart size={18} /> Cart ({cart})
        </button>
      </header>
      <main className="max-w-6xl mx-auto p-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* Products generated by NEXTGEN AI */}
        </div>
      </main>
    </div>
  );
}
                """.trimIndent()
            }

            generatedAppCode.value = code
            repository.recordGeneration(
                studio = if (isAndroid) "AI App Builder" else "AI Website Builder",
                prompt = appPrompt.value,
                title = if (isAndroid) "ApexStore Android App" else "NextGenStore React Web",
                summary = "Full source code • Production ready • Interactive preview enabled",
                cost = 25
            )
            isAppGenerating.value = false
        }
    }

    // --- Admin & Credit Controls ---
    fun grantAdminCredits(amount: Int) {
        viewModelScope.launch {
            repository.addCredits(amount, "Admin Manual Credit Injection (+$amount ⚡)")
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            if (_activeProject.value?.id == projectId) {
                _activeProject.value = null
                _activeScenes.value = emptyList()
            }
        }
    }
}
