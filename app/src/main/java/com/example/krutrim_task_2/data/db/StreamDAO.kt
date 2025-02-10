package com.example.krutrim_task_2.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface StreamDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<StreamEntity>)

    @Query("SELECT * FROM streamTable ORDER BY timestamp DESC LIMIT 100")
    fun getRecentItems(): Flow<List<StreamEntity>>

    @Query("DELETE FROM streamTable")
    suspend fun clearAll()
}
