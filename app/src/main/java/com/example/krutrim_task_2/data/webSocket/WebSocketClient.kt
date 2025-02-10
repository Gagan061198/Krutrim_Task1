package com.example.krutrim_task_2.data.webSocket

import android.util.Log
import com.example.krutrim_task_2.data.db.StreamEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray



class WebSocketClient(private val okHttpClient: OkHttpClient) {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<List<StreamEntity>>()
    val messages: SharedFlow<List<StreamEntity>> = _messages

    fun connect() {
        val request = Request.Builder()
            .url("wss://ws-api.coincheck.com/")
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("""{"type":"subscribe","channel":"btc_jpy-orderbook"}""")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Raw message: $text")
                try {
                    val items = parseMessage(text)
                    if (items.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Default).launch {
                            _messages.emit(items)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error processing message: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection failed: ${t.message}")
            }
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing: $code - $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closed: $code - $reason")
            }
        })
    }

    private fun parseMessage(text: String): List<StreamEntity> {
        return try {
            val jsonArray = JSONArray(text)
            val data = jsonArray.getJSONObject(1)

            val bids = data.getJSONArray("bids")
            val asks = data.getJSONArray("asks")

            val items = mutableListOf<StreamEntity>()

            for (i in 0 until bids.length()) {
                val bid = bids.getJSONArray(i)
                items.add(
                    StreamEntity(
                        type = "bid",
                        price = bid.getString(0),
                        amount = bid.getString(1)
                    )
                )
            }

            for (i in 0 until asks.length()) {
                val ask = asks.getJSONArray(i)
                items.add(
                    StreamEntity(
                        type = "ask",
                        price = ask.getString(0),
                        amount = ask.getString(1)
                    )
                )
            }

            items
        } catch (e: Exception) {
            Log.e("WebSocket", "Parsing failed: ${e.message}")
            emptyList()
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User requested disconnect")
    }
}