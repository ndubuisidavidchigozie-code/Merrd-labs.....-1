package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.model.MistakeItem
import com.example.model.ResearchNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ResearchDao {
  @Query("SELECT * FROM research_notes ORDER BY timestamp DESC")
  fun getAllNotes(): Flow<List<ResearchNote>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: ResearchNote): Long

  @Query("UPDATE research_notes SET likes = likes + 1 WHERE id = :id")
  suspend fun likeNote(id: Long)

  @Query("DELETE FROM research_notes WHERE id = :id")
  suspend fun deleteNote(id: Long)
}

@Dao
interface MistakeDao {
  @Query("SELECT * FROM cbt_mistakes WHERE cleared = 0 ORDER BY recordedAt DESC")
  fun getActiveMistakes(): Flow<List<MistakeItem>>

  @Query("SELECT * FROM cbt_mistakes WHERE category = :category AND cleared = 0 ORDER BY recordedAt DESC")
  fun getMistakesByCategory(category: String): Flow<List<MistakeItem>>

  @Query("SELECT * FROM cbt_mistakes ORDER BY recordedAt DESC")
  fun getAllMistakes(): Flow<List<MistakeItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMistake(item: MistakeItem): Long

  @Update
  suspend fun updateMistake(item: MistakeItem)

  @Query("UPDATE cbt_mistakes SET consecutiveCorrect = consecutiveCorrect + 1, cleared = CASE WHEN consecutiveCorrect + 1 >= 2 THEN 1 ELSE 0 END WHERE id = :id")
  suspend fun recordMistakeCorrect(id: Long)

  @Query("UPDATE cbt_mistakes SET consecutiveCorrect = 0 WHERE id = :id")
  suspend fun recordMistakeWrong(id: Long)

  @Query("DELETE FROM cbt_mistakes WHERE cleared = 1")
  suspend fun purgeClearedMistakes()
}

@Database(entities = [ResearchNote::class, MistakeItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun researchDao(): ResearchDao
  abstract fun mistakeDao(): MistakeDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "merdd_lab.db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
