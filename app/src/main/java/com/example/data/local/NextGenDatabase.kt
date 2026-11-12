package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.CreditTransactionEntity
import com.example.data.model.GenerationHistoryEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.VideoSceneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String)
}

@Dao
interface SceneDao {
    @Query("SELECT * FROM video_scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    fun getScenesForProject(projectId: String): Flow<List<VideoSceneEntity>>

    @Query("SELECT * FROM video_scenes WHERE projectId = :projectId ORDER BY sceneNumber ASC")
    suspend fun getScenesListForProject(projectId: String): List<VideoSceneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<VideoSceneEntity>)

    @Update
    suspend fun updateScene(scene: VideoSceneEntity)

    @Query("DELETE FROM video_scenes WHERE projectId = :projectId")
    suspend fun deleteScenesForProject(projectId: String)
}

@Dao
interface GenerationDao {
    @Query("SELECT * FROM generation_history ORDER BY timestamp DESC")
    fun getAllGenerations(): Flow<List<GenerationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneration(generation: GenerationHistoryEntity)
}

@Dao
interface CreditDao {
    @Query("SELECT * FROM credit_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<CreditTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CreditTransactionEntity)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE userId = :id LIMIT 1")
    fun getUserProfile(id: String = "user_default_01"): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE userId = :id LIMIT 1")
    suspend fun getUserProfileOnce(id: String = "user_default_01"): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}

@Database(
    entities = [
        ProjectEntity::class,
        VideoSceneEntity::class,
        GenerationHistoryEntity::class,
        CreditTransactionEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NextGenDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun sceneDao(): SceneDao
    abstract fun generationDao(): GenerationDao
    abstract fun creditDao(): CreditDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: NextGenDatabase? = null

        fun getDatabase(context: Context): NextGenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NextGenDatabase::class.java,
                    "nextgen_studio_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
