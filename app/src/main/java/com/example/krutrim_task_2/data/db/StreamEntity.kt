package com.example.krutrim_task_2.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streamTable")
data class StreamEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val price: String,
    val amount: String,
    val timestamp: Long = System.currentTimeMillis()
)