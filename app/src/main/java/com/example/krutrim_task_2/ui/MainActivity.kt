package com.example.krutrim_task_2.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.krutrim_task_2.data.db.StreamDatabase
import com.example.krutrim_task_2.data.db.StreamEntity
import com.example.krutrim_task_2.data.repositories.StreamRepository
import com.example.krutrim_task_2.data.webSocket.WebSocketClient
import okhttp3.OkHttpClient


class MainActivity : ComponentActivity() {
    private val viewModel: StreamViewModel by viewModels {
        StreamVMFactory(
            StreamRepository(
                WebSocketClient(OkHttpClient()),
                StreamDatabase.getDatabase(this)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    MainScreen(viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.stopStreaming()
        super.onDestroy()
    }
}

@Composable
fun StreamDataRV(items: List<StreamEntity>, lazyListState: LazyListState) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
//            key = { item -> (item.timestamp + item.id + item.amount.toDouble().roundToInt() + item.price.toDouble().roundToInt()).hashCode() }
        ) { item ->
            MainScreenLayout(item)
        }
    }
}

@Composable
fun MainScreenLayout(item: StreamEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.type.uppercase(),
            color = if (item.type == "ask") Color.Red else Color.Green,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Price: ${item.price}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "Amount: ${item.amount}",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun MainScreen(viewModel: StreamViewModel) {
    val streamDataItems by viewModel.streamedItems.collectAsState()
    val isStreaming by viewModel.isStreaming.collectAsState()
    //val errorMessage by viewModel.errorMessage.collectAsState()

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.startStreaming() },
                enabled = !isStreaming
            ) {
                Text("Start Streaming")
            }

            Button(
                onClick = { viewModel.stopStreaming() },
                enabled = isStreaming
            ) {
                Text("Stop Streaming")
            }
        }

/*        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }*/


        if (streamDataItems.isNotEmpty()) {
            StreamDataRV(streamDataItems, lazyListState)
        } else {
            Text(
                text = if (isStreaming) "Waiting for data..." else "Press Start to begin streaming",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}