package com.example.domain.director

import com.example.data.model.CinematicStyle
import com.example.data.model.Language
import com.example.data.model.VideoDuration
import com.example.data.model.VideoSceneEntity
import java.util.UUID

data class MovieScript(
    val title: String,
    val logline: String,
    val mainCharacters: List<MovieCharacter>,
    val scenes: List<VideoSceneEntity>,
    val themeMusicStyle: String,
    val totalEstimatedMinutes: Int
)

data class MovieCharacter(
    val name: String,
    val role: String,
    val visualAppearance: String,
    val voiceType: String,
    val arc: String
)

object MovieDirectorEngine {

    fun generateMovieProduction(
        prompt: String,
        duration: VideoDuration,
        language: Language,
        style: CinematicStyle,
        projectId: String = UUID.randomUUID().toString()
    ): MovieScript {
        val cleanPrompt = prompt.trim().ifEmpty { "A visionary youth rises from adversity to build an empire" }
        val title = deriveTitle(cleanPrompt)
        val logline = "A captivating ${style.label.lowercase()} narrative following the journey inspired by: \"$cleanPrompt\""

        // Character consistency definitions
        val characters = listOf(
            MovieCharacter(
                name = "Aarav Sharma",
                role = "Protagonist",
                visualAppearance = "Early 20s, sharp focused eyes, simple dark blue denim jacket with subtle silver pendant, weathered canvas bag",
                voiceType = "Warm, determined, slightly baritone with passionate resonance",
                arc = "From humble slum street vendor to pioneering tech architecture visionary"
            ),
            MovieCharacter(
                name = "Vikram Singhania",
                role = "Mentor / Tycoon",
                visualAppearance = "Mid 50s, impeccably tailored charcoal suit, silver hair at temples, piercing analytical gaze",
                voiceType = "Authoritative, calm, rich gravitas",
                arc = "Recognizes raw genius, challenges protagonist to achieve true greatness"
            ),
            MovieCharacter(
                name = "Priya Sen",
                role = "Co-Founder & Moral Anchor",
                visualAppearance = "Late 20s, expressive amber eyes, emerald blazer, wireframe glasses",
                voiceType = "Energetic, articulate, optimistic",
                arc = "Binds the vision together through every crucible"
            )
        )

        // Calculate scenes based on duration
        val sceneCount = when (duration) {
            VideoDuration.SEC_30 -> 3
            VideoDuration.MIN_1 -> 4
            VideoDuration.MIN_5 -> 5
            VideoDuration.MIN_10 -> 6
            VideoDuration.MIN_20 -> 8
            VideoDuration.MIN_30 -> 10
            VideoDuration.MIN_60 -> 12
            VideoDuration.CUSTOM -> 6
        }

        val totalSec = duration.seconds
        val stepSec = totalSec / sceneCount

        val sceneBlueprints = listOf(
            Triple(
                "The Slum Alley Dawn",
                "Low-Angle Tracking Shot moving through narrow rain-slicked gullies as morning mist breaks over rusted corrugated roofs.",
                Pair(
                    "People say fortune is written on palms. But if you have no hands, where is your destiny? It's in the fire within.",
                    "लोग कहते हैं कि किस्मत लकीरों में होती है। लेकिन जिनके हाथ नहीं होते, उनकी तकदीर कहाँ होती है? वो अंदर की आग में होती है।"
                )
            ),
            Triple(
                "The Silicon Workshop",
                "High-Speed Macro Zoom onto glowing motherboard traces and makeshift quantum circuit rigs illuminated by neon cyan LEDs.",
                Pair(
                    "One working prototype. That is all we need to break through the barrier.",
                    "सिर्फ एक काम करता प्रोटोटाइप... बस यही चाहिए दुनिया की सारी दीवारें गिराने के लिए।"
                )
            ),
            Triple(
                "Crucible of Rejection",
                "Dutch Angle Slow Pan across towering corporate skyscraper glass windows reflecting stormy dark clouds.",
                Pair(
                    "They laughed at our blueprint today. Tomorrow, they will stand in queue to license it.",
                    "आज वो हमारे नक्शे पर हँसे हैं। कल इसी विज़न को देखने के लिए कतार में खड़े होंगे।"
                )
            ),
            Triple(
                "The Breakthrough Night",
                "360 Degree Orbiting Steadicam as holographic server diagnostics turn emerald green and data streams cascade.",
                Pair(
                    "The neural model converged! Vikram sir, look at the telemetry—it worked!",
                    "न्यूरल मॉडल कन्वर्ज हो गया! विक्रम सर, टेलीमेट्री देखिए—हमारा सपना सच हो गया!"
                )
            ),
            Triple(
                "The Global Keynote",
                "Sweeping Aerial Crane Shot descending toward a packed amphitheater bathed in electric blue spotlights.",
                Pair(
                    "We did not build this to escape where we came from. We built this to show where anyone can go.",
                    "हमने यह तकनीक अपने अतीत से भागने के लिए नहीं बनाई। यह दिखाने के लिए बनाई कि कोई भी कहाँ तक पहुँच सकता है।"
                )
            ),
            Triple(
                "Return to the Roots",
                "Golden Hour Telephoto Close-Up as Aarav touches the worn brick wall of his childhood school with children cheering.",
                Pair(
                    "Never forget the soil that taught you how to stand when the storms came.",
                    "उस मिट्टी को कभी मत भूलना जिसने तुम्हें तूफानों में सिर उठाकर खड़े रहना सिखाया था।"
                )
            ),
            Triple(
                "The Legacy Horizon",
                "Panoramic Drone Pullback against a sprawling illuminated smart-city skyline under a starry night sky.",
                Pair(
                    "Every great reality began as a single impossible thought. Keep dreaming.",
                    "हर महान क्रांति की शुरुआत एक असंभव सोच से ही होती है। सपने देखना कभी मत छोड़ना।"
                )
            ),
            Triple(
                "Grand Finale - Infinite Tomorrow",
                "Epic Match-Cut dissolving from Aarav's eyes into a constellation of orbital space stations glowing in blue neon.",
                Pair(
                    "The future does not belong to those who wait. It belongs to those who dare to build it.",
                    "भविष्य उनका नहीं जो इंतज़ार करते हैं। भविष्य उनका है जो इसे खुद रचने की हिम्मत रखते हैं।"
                )
            ),
            Triple(
                "The High Stakes Negotiation",
                "Tense Over-The-Shoulder Shot framing Vikram's steely composure against a syndicate of international venture capitalists.",
                Pair(
                    "You are not buying software. You are buying the engine of the next century.",
                    "आप कोई सॉफ्टवेयर नहीं खरीद रहे। आप अगली सदी का सबसे शक्तिशाली इंजन खरीद रहे हैं।"
                )
            ),
            Triple(
                "The Final Victory March",
                "Ultra-Wide Heroic Tracking Shot as the whole team walks through the glass atrium amidst flashing cameras.",
                Pair(
                    "From Mumbai's alleys to the world's apex—this is just chapter one.",
                    "मुंबई की तंग गलियों से लेकर दुनिया के शिखर तक—यह तो बस पहला अध्याय है।"
                )
            ),
            Triple(
                "Under The Rain of Triumph",
                "Atmospheric Slow-Motion Close-Up in falling rain as neon reflections shimmer across Aarav's face.",
                Pair(
                    "Every tear of struggle has crystallized into our armor.",
                    "हर एक मुश्किल का आँसू आज हमारी सबसे मजबूत ढाल बन चुका है।"
                )
            ),
            Triple(
                "The New Dawn Anthem",
                "Cinematic Sunburst Flare as the sun rises over the completed headquarters tower.",
                Pair(
                    "Today, a new chapter begins for every dreamer who was told 'No'.",
                    "आज उन सभी हौसलों का नया सवेरा है जिन्हें कभी 'ना' कहा गया था।"
                )
            )
        )

        val generatedScenes = mutableListOf<VideoSceneEntity>()
        for (i in 0 until sceneCount) {
            val bp = sceneBlueprints[i % sceneBlueprints.size]
            val startTime = i * stepSec
            val endTime = (i + 1) * stepSec
            val char = characters[i % characters.size]

            val scene = VideoSceneEntity(
                projectId = projectId,
                sceneNumber = i + 1,
                title = "Scene ${i + 1}: ${bp.first}",
                timeStartSec = startTime,
                timeEndSec = endTime,
                location = bp.first,
                cameraAngle = bp.second,
                characterName = char.name,
                visualDescription = "Cinematic shot in ${style.label} aesthetic. Character ${char.name} (${char.visualAppearance}) captured in intense focus amidst atmospheric lighting.",
                dialogueText = bp.third.first,
                hindiDialogueText = bp.third.second,
                narrationAudioPrompt = "Narration voice with ${char.voiceType} delivering dialogue with emotional sincerity.",
                bgmMood = when (i % 4) {
                    0 -> "Atmospheric Emotive Strings & Subtle Cello (#Deep)"
                    1 -> "Building Pulse Electronic Beat (#Tension)"
                    2 -> "Soaring Cinematic Orchestral Crescendo (#Climax)"
                    else -> "Euphoric Ambient Synth & Choir (#Triumph)"
                },
                visualPrompt = "Hyper-realistic cinematic film still, 35mm anamorphic lens, 8k resolution, ${style.description}, ${bp.first}, character ${char.name}"
            )
            generatedScenes.add(scene)
        }

        return MovieScript(
            title = title,
            logline = logline,
            mainCharacters = characters,
            scenes = generatedScenes,
            themeMusicStyle = "Symphonic ${style.label} Original Score",
            totalEstimatedMinutes = duration.seconds / 60
        )
    }

    private fun deriveTitle(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("poor") && lower.contains("boy") -> "The Slumdog Emperor: Rise of Aarav"
            lower.contains("hindi") -> "Kismat Ki Lakeerein (Lines of Destiny)"
            lower.contains("sci-fi") || lower.contains("future") -> "Project Neo-Genesis: 2099"
            lower.contains("shopping") -> "Apex Retail: The Commerce Revolution"
            lower.contains("movie") -> "Echoes of the Crown"
            prompt.length in 5..35 -> prompt.replaceFirstChar { it.uppercase() }
            else -> "Genesis: The Unbroken Journey"
        }
    }
}
