package com.example.krutrim_task_2.data.repositories

import android.util.Log
import com.example.krutrim_task_2.data.db.StreamDatabase
import com.example.krutrim_task_2.data.db.StreamEntity
import com.example.krutrim_task_2.data.webSocket.WebSocketClient
import kotlinx.coroutines.flow.Flow


class StreamRepository(
    private val webSocketClient: WebSocketClient,
    private val db: StreamDatabase
) {
    fun getWebSocketMessages(): Flow<List<StreamEntity>> {
        webSocketClient.connect()
        return webSocketClient.messages
    }

    suspend fun insertItems(items: List<StreamEntity>) {
        try {
            db.streamDao().insertAll(items)
            Log.d("Repository", "Inserted ${items.size} items")
        } catch (e: Exception) {
            Log.e("Repository", "Insert failed: ${e.message}")
        }
    }

    fun getCachedItems(): Flow<List<StreamEntity>> {
        return db.streamDao().getRecentItems()
    }

    fun disconnectWebSocket() {
        webSocketClient.disconnect()
    }
}