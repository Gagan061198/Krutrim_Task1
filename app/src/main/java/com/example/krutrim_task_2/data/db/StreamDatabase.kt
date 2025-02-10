package com.example.krutrim_task_2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StreamEntity::class], version = 1, exportSchema = false)
abstract class StreamDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDAO

    companion object {
        @Volatile
        private var INSTANCE: StreamDatabase? = null

        fun getDatabase(context: Context): StreamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StreamDatabase::class.java,
                    "crypto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}