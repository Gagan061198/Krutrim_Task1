package com.example.krutrim_task_2.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krutrim_task_2.data.db.StreamEntity
import com.example.krutrim_task_2.data.repositories.StreamRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch



class StreamViewModel(
    private val repository: StreamRepository
) : ViewModel() {
    private val _streamedItems = MutableStateFlow<List<StreamEntity>>(emptyList())
    val streamedItems: StateFlow<List<StreamEntity>> = _streamedItems

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming

    private var webSocketJob: Job? = null

    fun startStreaming() {
        //if (_isStreaming.value) return

        _isStreaming.value = true
        webSocketJob = viewModelScope.launch {
            repository.getWebSocketMessages()
                .catch { e ->
                    Log.e("ViewModel", "Stream error: ${e.message}")
                    _isStreaming.value = false
                }
                .collect { items ->

                    Log.d("ViewModel", "Received ${items.size} items")

                    if (items.isNotEmpty()) {
                        _streamedItems.value = items
                        repository.insertItems(items)
                    }
                }
        }
    }

    fun stopStreaming() {
        _isStreaming.value = false
        webSocketJob?.cancel()
        repository.disconnectWebSocket()
        _streamedItems.value = emptyList()
    }

    init {
        loadCachedData()
    }

    private fun loadCachedData() {
        viewModelScope.launch {
            repository.getCachedItems().collect { cachedItems ->
                _streamedItems.value = cachedItems
            }
        }
    }
}