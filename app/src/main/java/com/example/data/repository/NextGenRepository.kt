package com.example.data.repository

import android.content.Context
import com.example.data.local.NextGenDatabase
import com.example.data.model.CinematicStyle
import com.example.data.model.CreditTransactionEntity
import com.example.data.model.GenerationHistoryEntity
import com.example.data.model.Language
import com.example.data.model.ProjectEntity
import com.example.data.model.StudioType
import com.example.data.model.UserProfileEntity
import com.example.data.model.VideoDuration
import com.example.data.model.VideoSceneEntity
import com.example.domain.director.MovieDirectorEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class NextGenRepository(private val context: Context) {
    private val database = NextGenDatabase.getDatabase(context)
    private val projectDao = database.projectDao()
    private val sceneDao = database.sceneDao()
    private val generationDao = database.generationDao()
    private val creditDao = database.creditDao()
    private val userProfileDao = database.userProfileDao()

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val allGenerations: Flow<List<GenerationHistoryEntity>> = generationDao.getAllGenerations()
    val allTransactions: Flow<List<CreditTransactionEntity>> = creditDao.getAllTransactions()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    private suspend fun seedInitialDataIfEmpty() {
        val currentProfile = userProfileDao.getUserProfileOnce()
        if (currentProfile == null) {
            val initialProfile = UserProfileEntity(
                userId = "user_default_01",
                email = "director@nextgen.ai",
                displayName = "Director Aryan",
                userLevel = "Studio Executive (Level 5)",
                creditsBalance = 1500,
                subscriptionPlan = "Pro Cinema Ultra",
                totalVideosGenerated = 8,
                totalImagesGenerated = 32,
                totalMusicGenerated = 12,
                totalAppsGenerated = 6,
                isAdmin = true
            )
            userProfileDao.insertOrUpdateProfile(initialProfile)

            // Seed initial credit transaction
            creditDao.insertTransaction(
                CreditTransactionEntity(
                    description = "Welcome Studio Bonus + VIP Tier Allocation",
                    amount = 1500,
                    balanceAfter = 1500
                )
            )

            // Seed featured project: "The Slumdog Emperor: Rise of Aarav" (20 min Hindi/Eng movie)
            val featuredMovieId = "proj_featured_movie_01"
            val featuredProject = ProjectEntity(
                id = featuredMovieId,
                title = "The Slumdog Emperor: Rise of Aarav",
                type = StudioType.LONG_VIDEO.name,
                prompt = "Create a 20-minute Hindi cinematic movie about a poor boy who becomes successful in AI technology.",
                status = "COMPLETED",
                durationSeconds = 1200,
                durationLabel = "20 Min Movie",
                language = Language.BILINGUAL.displayName,
                style = CinematicStyle.BOLLYWOOD_EPIC.label,
                resolution = "4K UHD Cinema",
                sceneCount = 8,
                creditCost = 180
            )
            projectDao.insertProject(featuredProject)

            val script = MovieDirectorEngine.generateMovieProduction(
                prompt = featuredProject.prompt,
                duration = VideoDuration.MIN_20,
                language = Language.BILINGUAL,
                style = CinematicStyle.BOLLYWOOD_EPIC,
                projectId = featuredMovieId
            )
            sceneDao.insertScenes(script.scenes)

            // Seed secondary showcase project: Sci-Fi Neon Metropolis
            val sciFiId = "proj_scifi_02"
            projectDao.insertProject(
                ProjectEntity(
                    id = sciFiId,
                    title = "Project Neo-Genesis: 2099",
                    type = StudioType.LONG_VIDEO.name,
                    prompt = "A 5-minute cyberpunk visual masterpiece exploring quantum artificial superintelligence in Neo-Tokyo.",
                    status = "COMPLETED",
                    durationSeconds = 300,
                    durationLabel = "5 Min Episode",
                    language = Language.ENGLISH.displayName,
                    style = CinematicStyle.HOLLYWOOD_SCIFI.label,
                    resolution = "1080p FHD",
                    sceneCount = 5,
                    creditCost = 75
                )
            )
            val sciFiScript = MovieDirectorEngine.generateMovieProduction(
                prompt = "A 5-minute cyberpunk visual masterpiece",
                duration = VideoDuration.MIN_5,
                language = Language.ENGLISH,
                style = CinematicStyle.HOLLYWOOD_SCIFI,
                projectId = sciFiId
            )
            sceneDao.insertScenes(sciFiScript.scenes)

            // Seed sample generations
            generationDao.insertGeneration(
                GenerationHistoryEntity(
                    studio = "AI Long Video",
                    prompt = "20-minute Hindi cinematic movie about Aarav",
                    title = "The Slumdog Emperor",
                    outputSummary = "8 Scenes • 4K UHD • Bilingual Audio • Orchestral Score",
                    creditsUsed = 180
                )
            )
            generationDao.insertGeneration(
                GenerationHistoryEntity(
                    studio = "AI Image Studio",
                    prompt = "Cyberpunk film still with electric blue neon reflections",
                    title = "Neo-Tokyo Cyber Clapper",
                    outputSummary = "DSLR 8K • 16:9 Cinema Aspect Ratio",
                    creditsUsed = 5
                )
            )
        }
    }

    suspend fun getScenesForProject(projectId: String): List<VideoSceneEntity> = withContext(Dispatchers.IO) {
        sceneDao.getScenesListForProject(projectId)
    }

    fun observeScenesForProject(projectId: String): Flow<List<VideoSceneEntity>> {
        return sceneDao.getScenesForProject(projectId)
    }

    suspend fun createMovieProject(
        prompt: String,
        duration: VideoDuration,
        language: Language,
        style: CinematicStyle
    ): Pair<ProjectEntity, List<VideoSceneEntity>> = withContext(Dispatchers.IO) {
        val projectId = UUID.randomUUID().toString()
        val script = MovieDirectorEngine.generateMovieProduction(
            prompt = prompt,
            duration = duration,
            language = language,
            style = style,
            projectId = projectId
        )

        val project = ProjectEntity(
            id = projectId,
            title = script.title,
            type = StudioType.LONG_VIDEO.name,
            prompt = prompt,
            status = "COMPLETED",
            durationSeconds = duration.seconds,
            durationLabel = duration.label,
            language = language.displayName,
            style = style.label,
            resolution = if (duration == VideoDuration.MIN_20 || duration == VideoDuration.MIN_30 || duration == VideoDuration.MIN_60) "4K UHD Cinema" else "1080p FHD",
            sceneCount = script.scenes.size,
            creditCost = duration.creditCost
        )

        projectDao.insertProject(project)
        sceneDao.insertScenes(script.scenes)

        // Deduct credits
        deductCredits(duration.creditCost, "Generated Movie: ${script.title}")

        // Log generation history
        generationDao.insertGeneration(
            GenerationHistoryEntity(
                studio = "AI Long Video",
                prompt = prompt,
                title = script.title,
                outputSummary = "${script.scenes.size} Scenes • ${duration.label} • ${language.displayName} • ${style.label}",
                creditsUsed = duration.creditCost
            )
        )

        Pair(project, script.scenes)
    }

    suspend fun recordGeneration(
        studio: String,
        prompt: String,
        title: String,
        summary: String,
        cost: Int
    ) = withContext(Dispatchers.IO) {
        deductCredits(cost, "$studio: $title")
        generationDao.insertGeneration(
            GenerationHistoryEntity(
                studio = studio,
                prompt = prompt,
                title = title,
                outputSummary = summary,
                creditsUsed = cost
            )
        )
    }

    suspend fun deductCredits(amount: Int, reason: String) = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce() ?: return@withContext
        val newBalance = (profile.creditsBalance - amount).coerceAtLeast(0)
        userProfileDao.insertOrUpdateProfile(profile.copy(creditsBalance = newBalance))
        creditDao.insertTransaction(
            CreditTransactionEntity(
                description = reason,
                amount = -amount,
                balanceAfter = newBalance
            )
        )
    }

    suspend fun addCredits(amount: Int, reason: String) = withContext(Dispatchers.IO) {
        val profile = userProfileDao.getUserProfileOnce() ?: return@withContext
        val newBalance = profile.creditsBalance + amount
        userProfileDao.insertOrUpdateProfile(profile.copy(creditsBalance = newBalance))
        creditDao.insertTransaction(
            CreditTransactionEntity(
                description = reason,
                amount = amount,
                balanceAfter = newBalance
            )
        )
    }

    suspend fun deleteProject(projectId: String) = withContext(Dispatchers.IO) {
        sceneDao.deleteScenesForProject(projectId)
        projectDao.deleteProject(projectId)
    }
}
