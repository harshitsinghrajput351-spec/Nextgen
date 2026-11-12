package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class StudioType {
    LONG_VIDEO,
    IMAGE,
    VOICE,
    MUSIC,
    APP_BUILDER,
    WEB_BUILDER,
    CREATIVE_TOOLS
}

enum class VideoDuration(val label: String, val seconds: Int, val creditCost: Int) {
    SEC_30("30s Teaser", 30, 20),
    MIN_1("1 Min Short", 60, 40),
    MIN_5("5 Min Episode", 300, 75),
    MIN_10("10 Min Feature", 600, 120),
    MIN_20("20 Min Movie", 1200, 180),
    MIN_30("30 Min Epic", 1800, 250),
    MIN_60("60 Min Cinema", 3600, 400),
    CUSTOM("Custom Duration", 900, 150)
}

enum class Language(val displayName: String, val code: String) {
    HINDI("Hindi (हिन्दी)", "hi"),
    ENGLISH("English (Global)", "en"),
    BILINGUAL("Hinglish / Bilingual", "hi-en")
}

enum class CinematicStyle(val label: String, val description: String) {
    BOLLYWOOD_EPIC("Bollywood Cinematic", "Grand emotional drama, high-contrast lighting, orchestral score"),
    HOLLYWOOD_SCIFI("Sci-Fi Cyberpunk", "Futuristic neon lighting, holographic VFX, synthesizer soundtrack"),
    NEO_NOIR("Neo-Noir Thriller", "Moody shadows, rain-slicked streets, suspenseful brass cues"),
    ANIME_MASTERPIECE("Anime Masterpiece", "Vibrant colors, hand-drawn anime aesthetic, soaring melody"),
    DOCUMENTARY_REALISM("Documentary Ultra-Real", "Handheld camera motion, naturalistic lighting, intimate narration")
}

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: String, // StudioType name
    val prompt: String,
    val status: String, // "COMPLETED", "PROCESSING", "DRAFT"
    val durationSeconds: Int,
    val durationLabel: String,
    val language: String,
    val style: String,
    val resolution: String = "1080p FHD",
    val sceneCount: Int = 0,
    val coverImageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val creditCost: Int = 0
)

@Entity(tableName = "video_scenes")
data class VideoSceneEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val sceneNumber: Int,
    val title: String,
    val timeStartSec: Int,
    val timeEndSec: Int,
    val location: String,
    val cameraAngle: String, // e.g. "Low-Angle Dolly In", "Aerial Drone Wide", "Close-Up Macro"
    val characterName: String,
    val visualDescription: String,
    val dialogueText: String,
    val hindiDialogueText: String,
    val narrationAudioPrompt: String,
    val bgmMood: String, // e.g. "Inspiring Strings", "Suspenseful Synth", "Heroic Brass"
    val visualPrompt: String,
    val isRendered: Boolean = true
)

@Entity(tableName = "generation_history")
data class GenerationHistoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val studio: String,
    val prompt: String,
    val title: String,
    val outputSummary: String,
    val creditsUsed: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS",
    val previewUrl: String? = null
)

@Entity(tableName = "credit_transactions")
data class CreditTransactionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val description: String,
    val amount: Int, // positive for topup, negative for usage
    val balanceAfter: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "user_default_01",
    val email: String = "creator@nextgen.ai",
    val displayName: String = "Apex Director",
    val userLevel: String = "VIP Studio Founder",
    val creditsBalance: Int = 1250,
    val subscriptionPlan: String = "Pro Studio Ultra",
    val totalVideosGenerated: Int = 14,
    val totalImagesGenerated: Int = 48,
    val totalMusicGenerated: Int = 9,
    val totalAppsGenerated: Int = 5,
    val isAdmin: Boolean = true
)

// In-Memory Representation for interactive generation steps
data class GenerationStage(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val isRunning: Boolean
)

data class GeneratedAppCode(
    val appName: String,
    val description: String,
    val targetType: String, // "Android Jetpack Compose" or "React + Tailwind Web"
    val mainFileContent: String,
    val secondaryFileContent: String,
    val sampleInteractiveState: String
)
