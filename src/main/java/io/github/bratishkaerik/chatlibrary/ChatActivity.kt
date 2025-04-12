package io.github.bratishkaerik.chatlibrary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal class ChatActivity : ComponentActivity() {
    private val client = OkHttpClient()
    private lateinit var ws: WebSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        val messagesCycle = findViewById<RecyclerView>(R.id.recycler)
        val input = findViewById<EditText>(R.id.input)
        val send = findViewById<Button>(R.id.send)

        messagesCycle.layoutManager = LinearLayoutManager(this)

        val adapter = MessageAdapter()
        messagesCycle.adapter = adapter

        ws = client.newWebSocket(
            request = Request.Builder()
                .url("wss://echo.websocket.org/")
                .build(),
            listener = object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    runOnUiThread {
                        if (text.contains("=") && text.contains("0x")) {
                            // this format: 203 = 0xcb
                            adapter.addMessage(Message("Returned from AFK.", Message.Sender.SERVER))
                        } else {
                            adapter.addMessage(Message(text.reversed().trim(), Message.Sender.SERVER))
                        }

                        messagesCycle.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        )

        send.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotBlank()) {
                ws.send(text)
                adapter.addMessage(Message(text.trim(), Message.Sender.CLIENT))
                messagesCycle.scrollToPosition(adapter.itemCount - 1)
                input.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ws.close(1000, null)
    }
}